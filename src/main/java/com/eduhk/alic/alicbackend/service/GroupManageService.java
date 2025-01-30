package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.dao.ChatGroupBotMapper;
import com.eduhk.alic.alicbackend.dao.GroupInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatBotInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.PasswordEntity;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.utils.Md5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public long createGroup(GroupInfoVO groupInfoVO, Long userId) {
        GroupInfoEntity groupInfoEntity = new GroupInfoEntity();
        if (groupInfoVO.getGroupType() == 0) {
            if (groupInfoVO.getPassword() == null) {
                return -1;
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
        groupInfoEntity.setGroupPortrait("");
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
                    entity.setGroupId((long) groupId);
                    return entity;
                })
                .collect(Collectors.toList());
        chatGroupBotMapper.insertBatch(chatBotInfoEntities);
    }

    public List<ChatBotInvokeVO> getGroupChatBotList(int groupId) {
        List<ChatBotInfoEntity> chatBotInfoEntities = chatGroupBotMapper.selectByGroupId((long) groupId);
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

    public GroupDemonVO getGroupInfo(Long groupId, Long userId) {

        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupId);
        GroupDemonVO groupDemonVO = new GroupDemonVO();

        groupDemonVO.setGroupName(groupInfoEntity.getGroupName());
        groupDemonVO.setGroupDescription(groupInfoEntity.getGroupDescription());
        groupDemonVO.setPortrait(groupInfoEntity.getGroupPortrait());
        groupDemonVO.setGroupType(groupInfoEntity.getGroupType());

        if (!Objects.equals(groupInfoEntity.getGroupAdmin(), userId)) {
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
}
