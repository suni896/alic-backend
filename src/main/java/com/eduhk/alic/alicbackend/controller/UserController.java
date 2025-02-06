package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.CurrentUser;
import com.eduhk.alic.alicbackend.model.vo.Result;
import com.eduhk.alic.alicbackend.model.vo.ResultResp;
import com.eduhk.alic.alicbackend.service.GroupUserService;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author FuSu
 * @date 2025/2/6 10:45
 */
@RestController
@Slf4j
@RequestMapping("/v1/user")
public class UserController {
    @Resource
    private GroupUserService groupUserService;

    //登出
    @PostMapping("/logout")
    public Result logout (@CurrentUser Long userId) {
        log.info("Logout:{}", userId);
        RedisUtils.delete(String.valueOf(userId));
        return ResultResp.success();
    }

    @GetMapping("/get_user_info")
    public Result getUserInfo(@CurrentUser Long userId) {
        log.info("getUserInfo:{}", userId);
        return ResultResp.success(groupUserService.getUserInfo(userId));
    }
}
