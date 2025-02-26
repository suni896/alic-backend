package com.eduhk.alic.alicbackend.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.service.LogInService;
import com.eduhk.alic.alicbackend.service.SmtpService;
import com.eduhk.alic.alicbackend.service.UserInfoService;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author FuSu
 * @date 2025/1/13 13:22
 */
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private static final String verfiCodeCachePrefix = "VERFI_CODE_";
    private static final String verifiTokenCachePrefix = "VERIFI_TOKEN_";

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
        //todo 关于邮件发送失败，这里改成异步了，所以要记录在db里面而不是返回错误码
        try {
            smtpService.sendMail(smtpVO, verfiCode);
        } catch (Exception e) {
            return ResultResp.failure(ResultCode.CODE_SEND_ERROR);
        }

        //redis记录验证码和生成时间 以备验证
        //TODO key的格式，设计+记录一下

        String cacheKey = verfiCodeCachePrefix + smtpVO.getType().getPrefix()+ smtpVO.getUserEmail();
        log.info("cacheKey,{}", cacheKey);
        RedisUtils.delete(cacheKey);
        RedisUtils.set(cacheKey, verfiCode, 10, TimeUnit.MINUTES);
        Map<String, String> resp = new HashMap<>();
        resp.put("email", smtpVO.getUserEmail());
        return ResultResp.success(resp);
    }
    //登录
    //1. 校验账号密码
    //2. 生成JWT并保存(到哪里？redis or mysql)
    //
    @PostMapping("/login")
    public Result login (@Validated @RequestBody LogInVO logInVO, HttpServletResponse response) {

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
            Cookie cookie = new Cookie("JWT_TOKEN", token);
//            cookie.setHttpOnly(true); // 增加 HttpOnly 防止客户端 JavaScript 访问
//            cookie.setSecure(true);   // 确保 cookie 在 HTTPS 下才能传输
            cookie.setPath("/v1/");      // 设置 cookie 的路径
            cookie.setMaxAge(4 * 60 * 60); // 设置过期时间（4小时）
            response.addCookie(cookie);   // 将 cookie 添加到响应中
            Map<String, String> resp = new HashMap<>();
            resp.put("Bearer Token", token);
            return ResultResp.success(resp);
        }else {
            return ResultResp.failure(ResultCode.PASSWORD_ERROR);
        }
    }

//    //注册
//    @PostMapping("/register")
//    public Result register (@Validated @RequestBody VerifiCodeVO verifiCodeVO) {
//
//        log.info("VerfiCodeVO:{}", verifiCodeVO);
//        //1. 校验验证码
//        String cacheKey = verfiCodeCachePrefix + verifiCodeVO.getType().getPrefix()+ verifiCodeVO.getEmail();
//        log.info("cacheKey,{}", cacheKey);
//        String verificode = RedisUtils.get(cacheKey);
//        if (verificode == null || !verificode.equals(verifiCodeVO.getVerifiCode())) {
//            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
//        }
//        UserInfoEntity user = userInfoService.getUnactiveUserInfoByEmail(verifiCodeVO.getEmail());
//        if (user == null) {
//            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
//        }
//        //2. 更新账号信息，状态改成1
//        userInfoService.activeAccount(verifiCodeVO.getEmail());
//        return ResultResp.success();
//    }

    @PostMapping("/verify_code")
    public Result verifycode (@Validated @RequestBody VerifiCodeVO verifiCodeVO) {

        log.info("VerfiCodeVO:{}", verifiCodeVO);
        //1. 校验验证码
        String cacheKey = verfiCodeCachePrefix + verifiCodeVO.getType().getPrefix() + verifiCodeVO.getEmail();
        log.info("cacheKey,{}", cacheKey);
        String verificode = RedisUtils.get(cacheKey);
        log.info("verificode,{}", verificode);
        if (verificode == null || !verificode.equals(verifiCodeVO.getVerifiCode())) {
            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
        }

        switch (verifiCodeVO.getType()){
//            case LOGIN -> {}
            case RESET -> {
                UserInfoEntity userActive = userInfoService.getActiveUserInfoByEmail(verifiCodeVO.getEmail());
                if (userActive == null) {
                    return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
                }
                String salt = RandomUtil.randomString(6);
                String token = SecureUtil.md5(verifiCodeVO.getEmail()+salt);
                String cacheKeyToken = verifiTokenCachePrefix + verifiCodeVO.getType().getPrefix() + verifiCodeVO.getEmail();
                log.info("cacheKey,{}", cacheKeyToken);
                RedisUtils.set(cacheKeyToken, token, 10, TimeUnit.MINUTES);
                Map<String, String> resp = new HashMap<>();
                resp.put("token", token);
                resp.put("email", verifiCodeVO.getEmail());
                return ResultResp.success(resp);
            }
            case REGISTERED -> {
                UserInfoEntity userUnactive = userInfoService.getUnactiveUserInfoByEmail(verifiCodeVO.getEmail());
                if (userUnactive == null) {
                    return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
                }
                //2. 更新账号信息，状态改成1
                userInfoService.activeAccount(verifiCodeVO.getEmail());
                Map<String, String> resp = new HashMap<>();
                resp.put("email", verifiCodeVO.getEmail());
                return ResultResp.success(resp);
            }
        }
        return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
    }

    @PostMapping("/reset")
    public Result reset (@Validated @RequestBody ResetVO resetVO) {

        log.info("ResetVO:{}", resetVO);
        //TODO 1. 校验校验码
        String cacheKeyToken = verifiTokenCachePrefix + resetVO.getType().getPrefix() + resetVO.getUserEmail();
        String token = RedisUtils.get(cacheKeyToken);
        if (token == null || !token.equals(resetVO.getToken())) {
            return ResultResp.failure(ResultCode.PARAMS_IS_INVALID);
        }
        //2. 重置密码
        userInfoService.resetPassword(resetVO);
        return ResultResp.success();
    }

}
