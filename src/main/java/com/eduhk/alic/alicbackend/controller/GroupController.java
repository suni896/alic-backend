package com.eduhk.alic.alicbackend.controller;

import cn.hutool.json.JSONObject;
import com.eduhk.alic.alicbackend.common.constant.GroupMemberType;
import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.service.GroupManageService;
import com.eduhk.alic.alicbackend.service.GroupSearchService;
import com.eduhk.alic.alicbackend.service.GroupUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    @Resource
    GroupUserService groupUserService;
    @Resource
    GroupSearchService groupSearchService;

    private static final Integer GROUP_MEMBER_LIMIT = 100;

    //创建群组
    @PostMapping("/create_new_group")
    public Result createGroup(@Validated @RequestBody GroupInfoVO groupInfoVO, @RequestAttribute("userId") Long userId) {
        Long newGroupId = groupManageService.createGroup(groupInfoVO, userId);
        groupUserService.addUserToGroup(newGroupId, userId);
        groupManageService.createGroupBot(groupInfoVO.getChatBotVOList(), newGroupId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("groupId",newGroupId);
        return ResultResp.success(jsonObject);
    }

    @PostMapping("/edit_group_info")
    public Result editGroup(@Validated @RequestBody GroupModifyInfoVO groupModifyInfoVO, @RequestAttribute("userId") Long userId) {
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupModifyInfoVO.getGroupId(), userId);
        if (!isExist) {
            return ResultResp.failure(ResultCode.USER_NOT_IN_GROUP);
        }
        GroupMemberType type = groupUserService.getGroupMemberType(groupModifyInfoVO.getGroupId(), userId);
        if (type == GroupMemberType.MEMBER) {
            return ResultResp.failure(ResultCode.NO_AUTH);
        }
        groupManageService.modifyGroupInfo(groupModifyInfoVO);
        return ResultResp.success();
    }
    //获取群组信息
    @GetMapping("/get_group_info")
    public Result getGroupInfo(@RequestParam("groupId") @NotNull(message = "groupId cannot be null")
                                   @Min(value = 1, message = "groupId must be greater than 0") Long groupId,
                               @RequestAttribute("userId") Long userId) {
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupId, userId);
        if (!isExist) {
            return ResultResp.failure(ResultCode.USER_NOT_IN_GROUP);
        }
        GroupMemberType type = groupUserService.getGroupMemberType(groupId, userId);
        GroupDemonVO groupDemonVO = groupManageService.getGroupInfo(groupId, type);
        return ResultResp.success(groupDemonVO);
    }

    //获取群组列表
    @PostMapping("/get_group_list")
    public Result getGroupList(@Validated @RequestBody GroupSearchVO groupSearchVO, @RequestAttribute("userId") Long userId) {
        PageVO<GroupSearchInfoVO> groupDemonVOPageVO = new PageVO<>();
        switch (groupSearchVO.getGroupDemonTypeEnum()){
            case ALLROOM -> {
                groupDemonVOPageVO = groupSearchService.searchAllGroup(groupSearchVO.getKeyword(), groupSearchVO.getPageRequestVO().getPageSize(), groupSearchVO.getPageRequestVO().getPageNum());
            }
            case JOINEDROOM -> {
                groupDemonVOPageVO = groupSearchService.searchJoinGroup(userId, groupSearchVO.getKeyword(),groupSearchVO.getPageRequestVO().getPageSize(), groupSearchVO.getPageRequestVO().getPageNum());
            }
            case PUBLICROOM -> {
                groupDemonVOPageVO = groupSearchService.searchPublicGroup(groupSearchVO.getKeyword(), groupSearchVO.getPageRequestVO().getPageSize(), groupSearchVO.getPageRequestVO().getPageNum());
            }
        }
        return ResultResp.success(groupDemonVOPageVO);
    }

    //获取群组成员列表
    @GetMapping("/get_group_member_list")
    public Result getGroupMemberList(@RequestParam("groupId") @NotNull(message = "groupId cannot be null")
                                         @Min(value = 1, message = "groupId must be greater than 0")Long groupId,
                                     @RequestAttribute("userId") Long userId) {
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupId, userId);
        if (!isExist) {
            return ResultResp.failure(ResultCode.USER_NOT_IN_GROUP);
        }
        //todo member在线情况
        return ResultResp.success(groupManageService.getMemberList(groupId));
    }

    //获取群组聊天机器人列表
    @GetMapping("/get_group_chat_bot_list")
    public Result getGroupChatBotList(@RequestParam("groupId") @NotNull(message = "groupId cannot be null")
                                          @Min(value = 1, message = "groupId must be greater than 0")Long groupId,
                                      @RequestAttribute("userId") Long userId) {

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
    public Result getRoleInGroup(@RequestParam("groupId") @NotNull(message = "groupId cannot be null")
                                     @Min(value = 1, message = "groupId must be greater than 0")Long groupId,
                                 @RequestAttribute("userId") Long userId) {
        GroupMemberType type = groupUserService.getGroupMemberType(groupId, userId);
        return ResultResp.success(type);
    }
    //添加成员
    @PostMapping("/add_group_member")
    public Result addGroupMember(@Validated @RequestBody GroupJoinVO groupJoinVO,
                                 @RequestAttribute("userId") Long userId) {
        //校验人数<=100
        Long memberCount = groupManageService.getMemberCount(groupJoinVO.getGroupId());
        if (memberCount >= GROUP_MEMBER_LIMIT) {
            return ResultResp.failure(ResultCode.GROUP_MEMBER_LIMIT);
        }
        //admin可添加member（预留逻辑）, member可添加自己
        groupUserService.verifyGroupAuth(groupJoinVO, userId);
        //被移除人是否在群里
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupJoinVO.getGroupId(), groupJoinVO.getJoinMemberID());
        if (isExist) {
            return ResultResp.failure(ResultCode.USER_ALREADY_IN_GROUP);
        }
        groupUserService.addGroupMember(groupJoinVO.getGroupId(), groupJoinVO.getJoinMemberID());
        return ResultResp.success();
    }
    //移除成员
    @PostMapping("/remove_group_member")
    public Result removeGroupMember(@Validated @RequestBody GroupRemoveVO groupRemoveVO,
                                    @RequestAttribute("userId") Long userId) {
//        groupUserService.verifyGroupExist(groupRemoveVO.getGroupId());

        //admin可移除member, member可移除自己
        groupUserService.verifyGroupAuth(groupRemoveVO, userId);
        //被移除人是否在群里
        Boolean isExist = groupUserService.verifyUserExistInGroup(groupRemoveVO.getGroupId(), groupRemoveVO.getRemoveMemberId());
        if (!isExist) {
            return ResultResp.failure(ResultCode.USER_NOT_IN_GROUP);
        }
        //ADMIN不可退群
        GroupMemberType removeType = groupUserService.getGroupMemberType(groupRemoveVO.getGroupId(), groupRemoveVO.getRemoveMemberId());
        if (removeType == GroupMemberType.ADMIN) {
            return ResultResp.failure(ResultCode.NO_AUTH);
        }

        groupUserService.removeGroupMember(groupRemoveVO.getGroupId(), groupRemoveVO.getRemoveMemberId());
        return ResultResp.success();
    }

}
