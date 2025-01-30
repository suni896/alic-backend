package com.eduhk.alic.alicbackend.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.dao.UserInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.PasswordEntity;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.ResetVO;
import com.eduhk.alic.alicbackend.model.vo.SmtpVO;
import com.eduhk.alic.alicbackend.utils.Md5Utils;
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

    public UserInfoEntity getActiveUserInfoByEmail(String email) {
        UserInfoEntity userInfoEntity = userInfoMapper.findUserByEmailAndCondition(email, 1);
        return userInfoEntity;
    }

    public UserInfoEntity getUnactiveUserInfoByEmail(String email) {
        UserInfoEntity userInfoEntity = userInfoMapper.findUserByEmailAndCondition(email, 0);
        return userInfoEntity;
    }

    public void saveUserInfo(SmtpVO smtpVO) {

        UserInfoEntity userInfoEntity = new UserInfoEntity();

        userInfoEntity.setUserEmail(smtpVO.getUserEmail());
        userInfoEntity.setUserName(smtpVO.getUserName());

        PasswordEntity passwordEntity = Md5Utils.addSalt(smtpVO.getPassword());
        userInfoEntity.setPassword(passwordEntity.getPassword());
        userInfoEntity.setSalt(passwordEntity.getSalt());

        userInfoEntity.setUserCondition(0);
        //TODO 头像生成
        userInfoEntity.setUserPortrait("");

        userInfoMapper.insert(userInfoEntity);
    }

    public void activeAccount(String email) {
        userInfoMapper.updateUserStatus(email, 1);
    }

    public void resetPassword(ResetVO resetVO) {
        // 生成随机盐
        String salt = RandomUtil.randomString(6);
        // 密码加盐后md5加密
        String md5Pwd = SecureUtil.md5(resetVO.getNewPassword()+salt);
        // 更新密码
        userInfoMapper.updatePassword(md5Pwd, salt, resetVO.getUserEmail());
    }

}
