package com.eduhk.alic.alicbackend.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.service.LogInService;
import com.eduhk.alic.alicbackend.service.SmtpService;
import com.eduhk.alic.alicbackend.service.UserInfoService;
import com.eduhk.alic.alicbackend.utils.JwtUtils;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author FuSu
 * @date 2025/1/13 13:22
 */
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private static String verfiCodeCachePrefix = "VERFI_CODE_";

    @Resource
    UserInfoService userInfoService;
    @Resource
    SmtpService smtpService;
    @Resource
    LogInService logInService;

    //发送验证码
    @PostMapping("/sendmail")
    public Result sendMail(@Validated @RequestBody SmtpVO smtpVO) {
        log.info("SmtpVO:{}", smtpVO);
        //todo 必须加限流
        //1. check account exist by email
        UserInfoEntity userActive = userInfoService.getActiveUserInfoByEmail(smtpVO.getUserEmail());
        switch (smtpVO.getType()){
//            case LOGIN -> {
//            }
            case RESET -> {
                if (userActive == null) {
                    return ResultResp.failure(ResultCode.UNREGISTERED_ACCOUNT);
                }
            }
            case REGISTERED -> {
                if (userActive != null) {
                    return ResultResp.failure(ResultCode.REGISTERED_ACCOUNT);
                }
                //记录账号信息到db
                //TODO  insert or update
                UserInfoEntity userUnactive = userInfoService.getUnactiveUserInfoByEmail(smtpVO.getUserEmail());
                if (userUnactive == null) {
                    userInfoService.saveUserInfo(smtpVO);
                }

            }
        }
        // 生成验证码
        String verfiCode = String.valueOf(RandomUtil.randomInt(100000, 999999));
        log.info("verfiCode,{}", verfiCode);

        //发送验证码
//        try {
//            smtpService.sendMail(smtpVO, verfiCode);
//        } catch (Exception e) {
//            return ResultResp.failure(ResultCode.CODE_SEND_ERROR);
//        }

        //redis记录验证码和生成时间 以备验证
        //TODO key的格式，设计+记录一下
        //TODO 可能要改成code作为key
        //TODO 如果发多次验证码是不是要强制过期之前的验证码？
        String cacheKey = verfiCodeCachePrefix + smtpVO.getType().getPrefix()+ verfiCode;
        log.info("cacheKey,{}", cacheKey);
        RedisUtils.set(cacheKey, smtpVO.getUserEmail(), 10, TimeUnit.MINUTES);

        return ResultResp.success();
    }
    //登录
    //1. 校验账号密码
    //2. 生成JWT并保存(到哪里？redis or mysql)
    //
    @PostMapping("/login")
    public Result login (@Validated @RequestBody LogInVO logInVO) {

        log.info("LogInVO:{}", logInVO);
        // 查询用户是否在数据库中
        UserInfoEntity user = userInfoService.getActiveUserInfoByEmail(logInVO.getUserEmail());

        // 判断用户是否异常
        if (user == null){
            return ResultResp.failure(ResultCode.UNREGISTERED_ACCOUNT);
        }

        // 校验密码-获取加密数据
        String salt = user.getSalt();
        String md5Pwd = SecureUtil.md5(logInVO.getPassword()+salt);
        // 校验密码
        if (user.getPassword().equals(md5Pwd)){
            String token = logInService.GenerateToken(user);
            //存储token
            String logInKey =  String.valueOf(user.getUserId());
            log.info("log in key,{}", logInKey);
            RedisUtils.delete(logInKey);
            RedisUtils.set(logInKey, token, 4, TimeUnit.HOURS);
            return ResultResp.success(token);
        }else {
            return ResultResp.failure(ResultCode.PASSWORD_ERROR);
        }
    }

    //注册
    @PostMapping("/register")
    public Result register (@Validated @RequestBody VerifiCodeVO verifiCodeVO) {

        log.info("VerfiCodeVO:{}", verifiCodeVO);
        //1. 校验验证码
        String cacheKey = verfiCodeCachePrefix + verifiCodeVO.getType().getPrefix()+ verifiCodeVO.getVerifiCode();
        log.info("cacheKey,{}", cacheKey);
        String email = RedisUtils.get(cacheKey);
        if (email == null) {
            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
        }
        UserInfoEntity user = userInfoService.getUnactiveUserInfoByEmail(email);
        if (user == null) {
            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
        }
        //2. 更新账号信息，状态改成1
        userInfoService.activeAccount(email);
        return ResultResp.success();
    }

    @PostMapping("/verifycode")
    public Result verifycode (@Validated @RequestBody VerifiCodeVO verifiCodeVO) {

        log.info("VerfiCodeVO:{}", verifiCodeVO);
        //1. 校验验证码
        String cacheKey = verfiCodeCachePrefix + verifiCodeVO.getType().getPrefix() + verifiCodeVO.getVerifiCode();
        log.info("cacheKey,{}", cacheKey);
        String email = RedisUtils.get(cacheKey);
        if (email == null) {
            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
        }
        switch (verifiCodeVO.getType()){
//            case LOGIN -> {}
            case RESET -> {
                UserInfoEntity userActive = userInfoService.getActiveUserInfoByEmail(email);
                if (userActive == null) {
                    return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
                }
                //TODO这里可能要加一个验证码给前端，防止被人篡改
                return ResultResp.success(email);
            }
            case REGISTERED -> {
                UserInfoEntity userUnactive = userInfoService.getUnactiveUserInfoByEmail(email);
                if (userUnactive == null) {
                    return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
                }
                //2. 更新账号信息，状态改成1
                userInfoService.activeAccount(email);
            }
        }
        return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
    }

    @PostMapping("/reset")
    public Result reset (@Validated @RequestBody ResetVO resetVO) {

        log.info("ResetVO:{}", resetVO);
        //TODO 1. 校验校验码
        //2. 重置密码
        userInfoService.resetPassword(resetVO);
        return ResultResp.success();
    }

    //登出
    @PostMapping("/logout")
    public Result logout (@RequestHeader("Authorization") String token) {
        log.info("Logout");
        String jwtToken = token.substring(7);
        String userId = JwtUtils.parseTokenUserId(jwtToken);
        RedisUtils.delete(userId);
        return ResultResp.success();
    }
}
