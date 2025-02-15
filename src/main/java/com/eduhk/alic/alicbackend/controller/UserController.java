package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.Result;
import com.eduhk.alic.alicbackend.model.vo.ResultResp;
import com.eduhk.alic.alicbackend.service.UserInfoService;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FuSu
 * @date 2025/2/6 10:45
 */
@RestController
@Slf4j
@RequestMapping("/v1/user")
public class UserController {
    @Resource
    private UserInfoService userInfoService;

    //登出
    @PostMapping("/logout")
    public Result logout (@RequestAttribute("userId") Long userId, HttpServletResponse response) {
        log.info("Logout:{}", userId);
        RedisUtils.delete(String.valueOf(userId));
        Cookie cookie = new Cookie("JWT_TOKEN", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResultResp.success();
    }

    @GetMapping("/get_user_info")
    public Result getUserInfo(@RequestAttribute("userId") Long userId) {
        log.info("getUserInfo:{}", userId);
        return ResultResp.success(userInfoService.getUserInfo(userId));
    }
}
