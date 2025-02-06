package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.common.constant.GroupMemberType;
import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.service.GroupManageService;
import com.eduhk.alic.alicbackend.service.GroupUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private GroupUserService groupUserService;

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

    @PostMapping("/edit_group_info")
    public Result editGroup(@Validated @RequestBody GroupInfoVO groupInfoVO, @CurrentUser Long userId) {
        return ResultResp.success();
    }
    //获取群组信息
    @GetMapping("/get_group_info")
    public Result getGroupInfo(@RequestParam("groupId") Long groupId, @CurrentUser Long userId) {
        GroupDemonVO groupDemonVO = groupManageService.getGroupInfo(groupId, userId);
        return ResultResp.success(groupDemonVO);
    }
    //获取群组列表
    @PostMapping("/get_group_list")
    public Result getGroupList(@Validated @RequestBody GroupListVO groupListVO, @CurrentUser Long userId) {
        switch (groupListVO.getGroupDemonTypeEnum()){
            case ALLROOM -> {}
            case JOINEDROOM -> {}
            case PUBLICROOM -> {
                //todo 分页
            }
        }
        return ResultResp.success();
    }
    //获取群组成员列表
    @GetMapping("/get_group_member_list")
    public Result getGroupMemberList(@RequestParam("groupId") Long groupId, @CurrentUser Long userId) {
        return ResultResp.success();
    }
    //获取群组聊天机器人列表
    @GetMapping("/get_group_chat_bot_list")
    public Result getGroupChatBotList(@RequestParam("groupId") Long groupId, @CurrentUser Long userId) {

        Boolean isExist = groupUserService.verifyUserExistInGroup(groupId, userId);
        if (!isExist) {
            return ResultResp.failure(ResultCode.USER_NOT_IN_GROUP);
        }
        GroupMemberType type = groupUserService.getGroupMemberType(groupId, userId);
        List<ChatBotInvokeVO> chatBotInvokeVOS = groupManageService.getGroupChatBotList(groupId, type);
        return ResultResp.success(chatBotInvokeVOS);
    }
    //获取当前成员身份：admin/member
    @GetMapping("/get_role_in_group")
    public Result getRoleInGroup(@RequestParam("groupId") Long groupId, @CurrentUser Long userId) {
        GroupMemberType type = groupUserService.getGroupMemberType(groupId, userId);
        return ResultResp.success(type);
    }
    //添加成员
    @PostMapping("/add_group_member")
    public Result addGroupMember(@Validated @RequestBody GroupJoinVO groupJoinVO,
                                 @CurrentUser Long userId) {
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupJoinVO.getGroupId(), groupJoinVO.getJoinMemberID());
        if (isExist) {
            //TODO
//            return ResultResp.failure();
        }
        groupUserService.verifyGroupAuth(groupJoinVO, userId);
        groupUserService.addGroupMember(groupJoinVO.getGroupId(), groupJoinVO.getJoinMemberID());
        return ResultResp.success();
    }
    //移除成员
    @PostMapping("/remove_group_member")
    public Result removeGroupMember(@Validated @RequestBody GroupRemoveVO groupRemoveVO,
                                    @CurrentUser Long userId) {
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupRemoveVO.getGroupId(), groupRemoveVO.getRemoveMemberId());
        if (!isExist) {
            return ResultResp.failure(ResultCode.USER_NOT_IN_GROUP);
        }
        groupUserService.verifyGroupAuth(groupRemoveVO, userId);
        groupUserService.removeGroupMember(groupRemoveVO.getGroupId(), groupRemoveVO.getRemoveMemberId());
        return ResultResp.success();
    }

}
