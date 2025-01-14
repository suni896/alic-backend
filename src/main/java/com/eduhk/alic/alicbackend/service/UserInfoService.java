package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.dao.UserInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FuSu
 * @date 2025/1/13 13:25
 */
@Service
@Slf4j
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserInfoEntity getUserInfoByEmail(String email) {
        UserInfoEntity userInfoEntity = userInfoMapper.findUserByEmailAndCondition(email, 1);
        return userInfoEntity;
    }
}
