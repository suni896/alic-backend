package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.Result;
import com.eduhk.alic.alicbackend.model.vo.ResultResp;
import com.eduhk.alic.alicbackend.model.vo.SmtpVO;
import com.eduhk.alic.alicbackend.service.SmtpService;
import com.eduhk.alic.alicbackend.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author FuSu
 * @date 2025/1/13 13:22
 */
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {
    @Resource
    UserInfoService userInfoService;
    @Resource
    SmtpService smtpService;
    @Autowired
    private CacheAutoConfiguration cacheAutoConfiguration;

    //发送验证码
    @PostMapping("/sendMail")
    public Result sendMail(@Validated @RequestBody SmtpVO smtpVO) {
        log.info("SmtpVO:{}", smtpVO);
        //1. check account exist by email
        UserInfoEntity user = userInfoService.getUserInfoByEmail(smtpVO.getUserEmail());
        switch (smtpVO.getType()){
            case LOGIN -> {
            }
            case RESET -> {
                if (user == null) {
                    return ResultResp.failure(ResultCode.UNREGISTERED_ACCOUNT);
                }
            }
            case REGISTERED -> {
                if (user != null) {
                    return ResultResp.failure(ResultCode.REGISTERED_ACCOUNT);
                }
                //记录账号信息到db
            }
        }

        //发送验证码
        try {
            smtpService.sendMail(smtpVO);
        } catch (Exception e) {
            return ResultResp.failure(ResultCode.CODE_SEND_ERROR);
        }

        //记录验证码和生成时间 以备验证

        return ResultResp.success();
    }
    //登录
    //1. 校验账号密码
    //2. 生成JWT并保存(到哪里？redis or mysql)
    //

    //注册
    //1. 校验验证码
    //2. 更新账号信息

    //重置密码
    //1. 校验验证码
    //2. 更新密码
}
