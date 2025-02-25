package com.eduhk.alic.alicbackend.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eduhk.alic.alicbackend.dao.ChatGroupUserMapper;
import com.eduhk.alic.alicbackend.dao.ChatMsgMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatGroupUserEntity;
import com.eduhk.alic.alicbackend.model.entity.ChatMsgEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupDetailInfoEntity;
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
    private static final Long offlineMsgRequestLength = 20L;

    // 插入新消息
    public long insertChatMsg(ChatMsgVO chatMsgVO) {

        ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
        chatMsgEntity.setMsgDest(chatMsgVO.getGroupId());
        chatMsgEntity.setMsgSource(chatMsgVO.getSenderId());
        chatMsgEntity.setMsgContent(chatMsgVO.getContent());
        chatMsgEntity.setMsgType(chatMsgVO.getMsgType());
        chatMsgEntity.setCreateTime(chatMsgVO.getCreateTime());
        chatMsgMapper.insertChatMsg(chatMsgEntity);
        return chatMsgEntity.getId();
    }


    public void sendMessageToOnlineMember(ChatMsgVO chatMsgVO, Long infoId) throws JsonProcessingException {
        //获取在线member列表
        List<Long> groupMemberIds = chatGroupUserMapper.selectMemberIdsByGroupId(chatMsgVO.getGroupId());
        ChatMsgRespVO respVO = new ChatMsgRespVO();
        respVO.setMsgType(chatMsgVO.getMsgType());
        respVO.setGroupId(chatMsgVO.getGroupId());
        respVO.setSenderId(chatMsgVO.getSenderId());
        respVO.setContent(chatMsgVO.getContent());
        respVO.setCreateTime(chatMsgVO.getCreateTime());
        respVO.setInfoId(infoId);

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
            //TODO接收客户端ack
            deleteMessageFromInbox(chatMsgVO.getGroupId(), onlineUserId, messageJson);
        }

    }
    // 将消息存储到群聊的收件箱中
    private void storeMessageToInbox(Long groupId, Long userId, String message) {
        String key = chatgroupMsgPrefix + groupId + "_" + userId;
        RedisUtils.lLeftPush(key, message);// 存储离线消息，使用左推（最新消息在最后面）
        RedisUtils.expire(key, 1, TimeUnit.DAYS);  // 设定离线消息的生命周期（例如 1 天）
    }

    private void deleteMessageFromInbox(Long groupId, Long userId, String message) {
        String key = chatgroupMsgPrefix + groupId + "_" + userId;
        RedisUtils.lRemove(key,0, message);
    }

    public List<ChatMsgRespVO> getReconnectOfflineMessages(Long groupId, Long userId, Long lastMsgId) {
        String key = chatgroupMsgPrefix + groupId + "_" + userId;
        Long inBoxLen = 0L;
        inBoxLen = RedisUtils.lLen(key);

        // 如果 Redis 中没有消息，或者消息已经过期，从数据库中加载消息
        if (inBoxLen == null || inBoxLen == 0) {
            List<ChatMsgEntity> offlineMsgList = new ArrayList<>();
            offlineMsgList = fetchMessagesFromDb(groupId, lastMsgId);
            return offlineMsgList.stream().map(msg -> {
                ChatMsgRespVO respVO = new ChatMsgRespVO();
                respVO.setMsgType(msg.getMsgType());
                respVO.setGroupId(msg.getMsgDest());
                respVO.setSenderId(msg.getMsgSource());
                respVO.setContent(msg.getMsgContent());
                respVO.setCreateTime(msg.getCreateTime());
                return respVO;
            }).toList();
        } else {
            List<String> offlineMsgList = new ArrayList<>();
            offlineMsgList = RedisUtils.lRange(key, 0, inBoxLen-1);
            return offlineMsgList.stream().map(msg -> {
                JSONObject jsonObject = new JSONObject(msg);
                return jsonObject.toBean(ChatMsgRespVO.class);
            }).toList();
        }

    }


    public List<ChatMsgRespVO> getOfflineMessages(Long groupId, Long userId, int pageNum, int pageSize) {
        List<ChatMsgEntity> offlineMsgList = new ArrayList<>();
        offlineMsgList = fetchMessagesFromDb(groupId, pageNum, pageSize);
        return offlineMsgList.stream().map(msg -> {
            ChatMsgRespVO respVO = new ChatMsgRespVO();
            respVO.setMsgType(msg.getMsgType());
            respVO.setGroupId(msg.getMsgDest());
            respVO.setSenderId(msg.getMsgSource());
            respVO.setContent(msg.getMsgContent());
            respVO.setCreateTime(msg.getCreateTime());
            return respVO;
        }).toList();
    }

    private List<ChatMsgEntity> fetchMessagesFromDb(Long groupId, Long lastMsgId) {
        List<ChatMsgEntity> offlineMsgList = new ArrayList<>();
        offlineMsgList = chatMsgMapper.getMessagesByIdGreaterThan(groupId, lastMsgId);
        return offlineMsgList;
    }

    private List<ChatMsgEntity> fetchMessagesFromDb(Long groupId, int pageNum, int pageSize) {

        IPage<ChatMsgEntity> page = new Page<>(pageNum, pageSize);
        Page<ChatMsgEntity> chatMsgEntityList = chatMsgMapper.getChatMsgByPage(groupId, page);
        return chatMsgEntityList.getRecords();
    }
}
