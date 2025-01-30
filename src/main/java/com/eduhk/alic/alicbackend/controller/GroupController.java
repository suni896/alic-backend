package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.service.GroupManageService;
import com.eduhk.alic.alicbackend.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 14:58
 */
@RestController
@Slf4j
@RequestMapping("/v1/group")
public class GroupController {

    @Resource
    GroupManageService groupManageService;

    //创建群组
    @PostMapping("/create_new_group")
    public Result createGroup(@Validated @RequestBody GroupInfoVO groupInfoVO, @CurrentUser Long userId) {
        long newGroupId = groupManageService.createGroup(groupInfoVO, userId);
        if (newGroupId < 0) {
            return ResultResp.failure(ResultCode.PARAMS_IS_BLANK);
        }
        groupManageService.createGroupBot(groupInfoVO.getChatBotVOList(), newGroupId);
        return ResultResp.success(newGroupId);
    }
    //获取群组信息
    @GetMapping("/get_group_info")
    public Result getGroupInfo(@RequestParam("groupId") Integer groupId, @CurrentUser Long userId) {
        GroupDemonVO groupDemonVO = groupManageService.getGroupInfo(Long.valueOf(groupId), userId);
        return ResultResp.success(groupDemonVO);
    }
    //获取群组列表
    @GetMapping("/get_group_list")
    public Result getGroupList() {
        return ResultResp.success();
    }
    //获取群组成员列表
    @GetMapping("/get_group_member_list")
    public Result getGroupMemberList(@RequestParam("groupId") Integer groupId) {
        return ResultResp.success();
    }
    //获取群组聊天机器人列表
    @GetMapping("/get_group_chat_bot_list")
    public Result getGroupChatBotList(@RequestParam("groupId") Integer groupId) {
        List<ChatBotInvokeVO> chatBotInvokeVOS =  groupManageService.getGroupChatBotList(groupId);
        return ResultResp.success(chatBotInvokeVOS);
    }
    //获取当前成员身份：admin/member
    @GetMapping("/get_role_in_group")
    public Result getRoleInGroup(@RequestParam("groupId") Integer groupId, @CurrentUser Long userId) {
        return ResultResp.success();
    }
    //添加成员
    @PostMapping("/add_group_member")
    public Result addGroupMember(@RequestParam("groupId") Integer groupId, @RequestParam("userId") Integer userId) {
        return ResultResp.success();
    }
    //移除成员
    @PostMapping("/remove_group_member")
    public Result removeGroupMember(@RequestParam("groupId") Integer groupId, @RequestParam("userId") Integer userId) {
        return ResultResp.success();
    }


}
