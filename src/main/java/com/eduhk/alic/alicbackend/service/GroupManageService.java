package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.common.constant.GroupMemberType;
import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.common.exception.BaseException;
import com.eduhk.alic.alicbackend.dao.ChatGroupBotMapper;
import com.eduhk.alic.alicbackend.dao.GroupInfoMapper;
import com.eduhk.alic.alicbackend.dao.UserInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.*;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.utils.AvatarUtils;
import com.eduhk.alic.alicbackend.utils.Md5Utils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author FuSu
 * @date 2025/1/21 16:46
 */
@Service
public class GroupManageService {
    @Resource
    private GroupInfoMapper groupInfoMapper;
    @Resource
    private ChatGroupBotMapper chatGroupBotMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    public long createGroup(GroupInfoVO groupInfoVO, Long userId) {
        GroupInfoEntity groupInfoEntity = new GroupInfoEntity();
        if (groupInfoVO.getGroupType() == 0) {
            if (groupInfoVO.getPassword() == null) {
                throw new BaseException(ResultCode.PARAMS_IS_INVALID);
            } else {
                PasswordEntity passwordEntity = Md5Utils.addSalt(groupInfoVO.getPassword());
                groupInfoEntity.setPassword(passwordEntity.getPassword());
                groupInfoEntity.setSalt(passwordEntity.getSalt());
            }
        }
        groupInfoEntity.setGroupType(groupInfoVO.getGroupType());
        groupInfoEntity.setGroupName(groupInfoVO.getGroupName());
        groupInfoEntity.setGroupDescription(groupInfoVO.getGroupDescription());
        // TODO 头像生成
//        groupInfoEntity.setGroupPortrait("");
        groupInfoEntity.setGroupAdmin(userId);
        groupInfoMapper.insert(groupInfoEntity);
        return groupInfoEntity.getGroupId();

    }

    public void createGroupBot(List<ChatBotVO> chatBotVOList, long groupId) {
        List<ChatBotInfoEntity> chatBotInfoEntities = chatBotVOList.stream()
                .map(vo -> {
                    //TODO 调用azure openai创建assistant
                    ChatBotInfoEntity entity = new ChatBotInfoEntity();
                    entity.setBotName(vo.getBotName());
                    entity.setBotPrompt(vo.getBotPrompt());
                    entity.setBotContext(vo.getBotContext());
                    entity.setAccessType(vo.getAccessType());
                    entity.setGroupId(groupId);
                    return entity;
                })
                .collect(Collectors.toList());
        chatGroupBotMapper.insertBatch(chatBotInfoEntities);
    }

    public List<ChatBotInvokeVO> getGroupChatBotList(long groupId, GroupMemberType type) {
        List<ChatBotInfoEntity> chatBotInfoEntities = new ArrayList<>();
        switch (type) {
            case ADMIN -> {
                chatBotInfoEntities = chatGroupBotMapper.selectByGroupId(groupId);
            }
            case MEMBER -> {
                chatBotInfoEntities = chatGroupBotMapper.selectByGroupIdMember(groupId);
            }
        }

        List<ChatBotInvokeVO> chatBotInvokeVOS = chatBotInfoEntities.stream().map(
                vo -> {
                    ChatBotInvokeVO chatBotDemonVO = new ChatBotInvokeVO();
                    chatBotDemonVO.setBotId(vo.getBotId());
                    chatBotDemonVO.setBotName(vo.getBotName());
                    chatBotDemonVO.setAccessType(vo.getAccessType());
                    return chatBotDemonVO;
                }
        ).collect(Collectors.toList());
        return chatBotInvokeVOS;
    }

    public GroupDemonVO getGroupInfo(Long groupId, GroupMemberType type) {

        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupId);
        GroupDemonVO groupDemonVO = new GroupDemonVO();
        groupDemonVO.setGroupId(groupInfoEntity.getGroupId());
        groupDemonVO.setGroupName(groupInfoEntity.getGroupName());
        groupDemonVO.setGroupDescription(groupInfoEntity.getGroupDescription());
        //todo 头像
//        groupDemonVO.setPortrait(groupInfoEntity.getGroupPortrait());
        groupDemonVO.setGroupType(groupInfoEntity.getGroupType());
        groupDemonVO.setPassword(groupInfoEntity.getPassword());

        if (type == GroupMemberType.MEMBER) {
            return groupDemonVO;
        }

        List<ChatBotInfoEntity> chatBotInfoEntities = chatGroupBotMapper.selectByGroupId(groupId);
        List<ChatBotDemonVO> chatBotDemonVOList = chatBotInfoEntities.stream().map(
                vo -> {
                    ChatBotDemonVO chatBotDemonVO = new ChatBotDemonVO();
                    chatBotDemonVO.setBotId(vo.getBotId());
                    chatBotDemonVO.setBotName(vo.getBotName());
                    chatBotDemonVO.setBotPrompt(vo.getBotPrompt());
                    chatBotDemonVO.setBotContext(vo.getBotContext());
                    chatBotDemonVO.setAccessType(vo.getAccessType());
                    return chatBotDemonVO;
                }
        ).collect(Collectors.toList());
        groupDemonVO.setChatBots(chatBotDemonVOList);
        return groupDemonVO;
    }

    public void modifyGroupInfo(GroupModifyInfoVO groupModifyInfoVO) {
        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupModifyInfoVO.getGroupId());
        //group是否存在
        if (groupInfoEntity == null) {
            throw new BaseException(ResultCode.GROUP_NOT_EXIST);
        }
        //group信息只有description和password可以修改
        //group信息在展示的时候，password返回给前端加salt之后的值，在这里判断的时候如果password没修改则与数据库中salt之后的值一致，否则就是被改过了。
        if (!Objects.equals(groupModifyInfoVO.getGroupDescription(), groupInfoEntity.getGroupDescription()) ||
                !Objects.equals(groupModifyInfoVO.getPassword(), groupInfoEntity.getPassword())) {

            // 更新群组描述
            groupInfoEntity.setGroupDescription(groupModifyInfoVO.getGroupDescription());

            // 处理密码更新
            String newPassword = groupModifyInfoVO.getPassword();
            if (!Objects.equals(newPassword, groupInfoEntity.getPassword())) {
                groupInfoEntity.setPassword(Md5Utils.addSalt(newPassword, groupInfoEntity.getSalt()).getPassword());
            }

            // 更新数据库
            groupInfoMapper.update(groupInfoEntity);
        }

        List<Long> oldBotLdList = chatGroupBotMapper.selectBotLdByGroupId(groupModifyInfoVO.getGroupId());
        List<Long> modifyBotLdList = groupModifyInfoVO.getModifyChatBotVOS().stream().map(ChatBotDemonVO::getBotId).toList();
        System.out.println(modifyBotLdList.get(0));
        // 找出相同元素,update
        for (ChatBotDemonVO chatBotModifyVO : groupModifyInfoVO.getModifyChatBotVOS()) {
            ChatBotInfoEntity chatBotInfoEntity = chatGroupBotMapper.selectById(chatBotModifyVO.getBotId());
            if (Objects.equals(chatBotInfoEntity.getBotContext(), chatBotModifyVO.getBotContext()) &&
            Objects.equals(chatBotInfoEntity.getAccessType(), chatBotModifyVO.getAccessType()) &&
            Objects.equals(chatBotInfoEntity.getBotPrompt(), chatBotModifyVO.getBotPrompt())) {

            }else {
                //TODO 调用azure openai修改assistant
                ChatBotInfoEntity modifyChatBotInfoEntity = new ChatBotInfoEntity();
                modifyChatBotInfoEntity.setBotId(chatBotModifyVO.getBotId());
                modifyChatBotInfoEntity.setAccessType(chatBotModifyVO.getAccessType());
                modifyChatBotInfoEntity.setBotContext(chatBotModifyVO.getBotContext());
                modifyChatBotInfoEntity.setBotPrompt(chatBotModifyVO.getBotPrompt());
                chatGroupBotMapper.update(modifyChatBotInfoEntity);
            }
        }

        //找出删除的bot,delete
        List<Long> deleteBotList = oldBotLdList.stream()
                .filter(e -> !modifyBotLdList.contains(e))
                .toList();
        if (!deleteBotList.isEmpty()) {
            chatGroupBotMapper.batchDeleteByIds(deleteBotList);
        }


        //新增的bot,insert
        List<ChatBotVO> newChatBotInfoVOS = groupModifyInfoVO.getAddChatBotVOList();
        if (newChatBotInfoVOS == null || newChatBotInfoVOS.isEmpty()) {
            return;
        }
        List<ChatBotInfoEntity> newChatBotInfoEntities = newChatBotInfoVOS.stream()
                .map(vo -> {
                    //TODO 调用azure openai创建assistant
                    ChatBotInfoEntity entity = new ChatBotInfoEntity();
                    entity.setBotName(vo.getBotName());
                    entity.setBotPrompt(vo.getBotPrompt());
                    entity.setBotContext(vo.getBotContext());
                    entity.setAccessType(vo.getAccessType());
                    entity.setGroupId(groupModifyInfoVO.getGroupId());
                    return entity;
                }).toList();
        chatGroupBotMapper.insertBatch(newChatBotInfoEntities);


    }

    public List<GroupMemberInfoVO> getMemberList(Long groupId) {
        List<GroupMemberInfoEntity> userInfoEntities = userInfoMapper.getUsersByGroupId(groupId);
        List<GroupMemberInfoVO> userInfoVOS = userInfoEntities.stream().map(
                entity ->{
                    GroupMemberInfoVO userInfoVO = new GroupMemberInfoVO();
                    userInfoVO.setUserId(entity.getUserId());
                    userInfoVO.setUserName(entity.getUserName());
                    userInfoVO.setUserEmail(entity.getUserEmail());
                    String userPortrait = AvatarUtils.transferToBase64(entity.getUserPortrait());
                    userInfoVO.setUserPortrait(userPortrait);
                    if (Objects.equals(entity.getAdminId(), entity.getUserId())){
                        userInfoVO.setGroupMemberType(GroupMemberType.ADMIN);
                    }else {
                        userInfoVO.setGroupMemberType(GroupMemberType.MEMBER);
                    }
                    return userInfoVO;
                }
        ).toList();
        return userInfoVOS;
    }

    public Long getMemberCount(Long groupId) {
        return userInfoMapper.getUserCountByGroupId(groupId);
    }
}
