package com.eduhk.alic.alicbackend.service;

import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.utils.JwtUtils;
import org.springframework.stereotype.Service;

/**
 * @author FuSu
 * @date 2025/1/16 12:55
 */
@Service
public class LogInService {
    public String GenerateToken(UserInfoEntity user) {

        String token  = JwtUtils.createToken(user.getUserId(), user.getUserEmail(), user.getUserName());
        return token;
    }
}
