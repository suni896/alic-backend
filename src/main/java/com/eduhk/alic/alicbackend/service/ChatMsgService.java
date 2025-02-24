package com.eduhk.alic.alicbackend.service;

import cn.hutool.json.JSONObject;
import com.eduhk.alic.alicbackend.dao.ChatGroupUserMapper;
import com.eduhk.alic.alicbackend.dao.ChatMsgMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatGroupUserEntity;
import com.eduhk.alic.alicbackend.model.entity.ChatMsgEntity;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgRespVO;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author FuSu
 * @date 2025/2/20 15:25
 */
@Service
public class ChatMsgService {
    @Resource
    ChatMsgMapper chatMsgMapper;

    @Resource
    ChatGroupUserMapper chatGroupUserMapper;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    private static final String chatgroupConnectPrefix = "CHATGROUP_CONNECT_";
    private static final String chatgroupMsgPrefix = "CHATGROUP_USERID_MSG_";

    // 插入新消息
    public void insertChatMsg(ChatMsgVO chatMsgVO) {

        ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
        chatMsgEntity.setMsgDest(chatMsgVO.getGroupId());
        chatMsgEntity.setMsgSource(chatMsgVO.getSenderId());
        chatMsgEntity.setMsgContent(chatMsgVO.getContent());
        chatMsgEntity.setMsgType(chatMsgVO.getMsgType());
        chatMsgEntity.setCreateTime(chatMsgVO.getCreateTime());
        chatMsgMapper.insertChatMsg(chatMsgEntity);

    }

    //todo 拉取固定条数的历史消息
//    public List<ChatMsgRespVO> getOfflineMessagesForUser(Long groupId, Date retriveTime) {
//        List<ChatMsgEntity> chatMsgEntityList =  chatMsgMapper.getOfflineMessagesForUser(groupId, retriveTime);
//
//    }

    public void sendMessageToOnlineMember(ChatMsgVO chatMsgVO) throws JsonProcessingException {
        //获取在线member列表
        List<Long> groupMemberIds = chatGroupUserMapper.selectMemberIdsByGroupId(chatMsgVO.getGroupId());
        ChatMsgRespVO respVO = new ChatMsgRespVO();
        respVO.setMsgType(chatMsgVO.getMsgType());
        respVO.setGroupId(chatMsgVO.getGroupId());
        respVO.setSenderId(chatMsgVO.getSenderId());
        respVO.setContent(chatMsgVO.getContent());
        respVO.setCreateTime(chatMsgVO.getCreateTime());

        ObjectMapper mapper = new ObjectMapper();
        String messageJson = mapper.writeValueAsString(respVO);

        for (Long groupMemberId : groupMemberIds) {
            storeMessageToInbox(chatMsgVO.getGroupId(), groupMemberId, messageJson);
        }
        Set<String> onlineUserIds = RedisUtils.setMembers(chatgroupConnectPrefix+chatMsgVO.getGroupId());
        List<Long> offlineMemberIds = new ArrayList<>(groupMemberIds);
        // 移除那些在线的成员,得到离线的成员
        offlineMemberIds.removeAll(onlineUserIds.stream()
                .map(Long::parseLong).toList());
        //给在线用户发送消息
        for (Long onlineUserId : offlineMemberIds) {
            messagingTemplate.convertAndSendToUser(onlineUserId.toString(), "/queue/messages"+chatMsgVO.getGroupId(), chatMsgVO);
            //TODO接收客户端ack，并删除对应收件箱的信息
        }

    }
    // 将消息存储到群聊的收件箱中
    public void storeMessageToInbox(Long groupId, Long userId, String message) {
        String key = chatgroupMsgPrefix + groupId + "_" + userId;
        RedisUtils.lLeftPush(key, message);// 存储离线消息，使用左推（最新消息在最后面）
        RedisUtils.expire(key, 1, TimeUnit.DAYS);  // 设定离线消息的生命周期（例如 1 天）
    }




}
