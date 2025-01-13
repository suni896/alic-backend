package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.Result;
import com.eduhk.alic.alicbackend.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
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

    //发送验证码
//    @PostMapping("/sendMail")
//    public Result<> sendMail(@Validated @RequestBody )
    //登录

    //注册

    //重置密码

    //
}
