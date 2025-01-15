package com.eduhk.alic.alicbackend.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.dao.UserInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.SmtpVO;
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

    public void saveUserInfo(SmtpVO smtpVO) {

        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserEmail(smtpVO.getUserEmail());
        userInfoEntity.setUserName(smtpVO.getUserName());
        String salt = RandomUtil.randomString(6); // hutool随机生成6个字符
        userInfoEntity.setSalt(salt);
        String md5Pwd = SecureUtil.md5(smtpVO.getPassword()+salt);
        userInfoEntity.setPassword(md5Pwd);
        userInfoEntity.setUserCondition(0);
        //TODO 头像生成
        userInfoEntity.setUserPortrait("");

        userInfoMapper.insert(userInfoEntity);
    }
}
