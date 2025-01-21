package com.eduhk.alic.alicbackend.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FuSu
 * @date 2025/1/16 12:13
 */
@Slf4j
@Component
public class JwtUtils {
    @Value("${jwt.config.key}")
    private String JwtSaltValue;

    @Value("${jwt.config.expire}")
    private String JwtExpireValue;

    private static String JwtSalt;
    private static String JwtExpire;

    @PostConstruct
    public void initJwtSalt() {
        JwtSalt = this.JwtSaltValue;
    }
    @PostConstruct
    public void initJwtExpire() {
        JwtExpire = this.JwtExpireValue;
    }

    public static String createToken(Long userId, String userEmail, String userName) {

        DateTime now = DateTime.now();
//        Map<String, Object> header = new HashMap<>();
//        header.put("typ", "JWT");
//        header.put("alg", "HS256");
        Map<String, Object> payload = new HashMap<>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, now.offsetNew(DateField.HOUR,4));
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 内容
        payload.put("userId", userId);
        payload.put("userEmail", userEmail);
        payload.put("userName", userName);
        log.info("JwtSalt:{}", JwtSalt);
        String token = JWTUtil.createToken(payload, JwtSalt.getBytes());
        log.info("生成JWT token：{}", token);
        return token;
    }
    public static boolean validate(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);

            boolean verifyKey = jwt.setKey(JwtSalt.getBytes()).verify();
            if (!verifyKey) {
                return false;
            }

            boolean verifyTime = jwt.validate(0);
            return verifyTime;
        }catch (Exception e) {
            log.info("JWT token校验异常", e);
            return false;
        }
    }

    public static String parseTokenUserId(String token) {
        JSONObject payLoadJson = JWTUtil.parseToken(token).setKey(JwtSalt.getBytes()).getPayload().getClaimsJson();
        return payLoadJson.getStr("userId");
    }

    public static String parseTokenUserEmail(String token) {
        JSONObject payLoadJson = JWTUtil.parseToken(token).setKey(JwtSalt.getBytes()).getPayload().getClaimsJson();
        return payLoadJson.getStr("userEmail");
    }
}
