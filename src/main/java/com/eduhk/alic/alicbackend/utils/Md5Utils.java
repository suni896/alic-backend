package com.eduhk.alic.alicbackend.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.model.entity.PasswordEntity;

/**
 * @author FuSu
 * @date 2025/1/20 11:37
 */
public class Md5Utils {
    public static PasswordEntity addSalt(String password) {
        // 生成随机盐
        String salt = RandomUtil.randomString(6);
        // 密码加盐后md5加密
        String md5Pwd = SecureUtil.md5(password+salt);
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setSalt(salt);
        passwordEntity.setPassword(md5Pwd);
        return passwordEntity;
    }

    public static PasswordEntity addSalt(String password, String salt) {

        // 密码加盐后md5加密
        String md5Pwd = SecureUtil.md5(password+salt);
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setSalt(salt);
        passwordEntity.setPassword(md5Pwd);
        return passwordEntity;
    }
}
