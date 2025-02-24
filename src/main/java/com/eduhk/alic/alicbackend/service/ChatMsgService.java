package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.dao.ChatMsgMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatMsgEntity;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgRespVO;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/20 15:25
 */
@Service
public class ChatMsgService {
    @Resource
    private ChatMsgMapper chatMsgMapper;

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
    public List<ChatMsgRespVO> getOfflineMessagesForUser(Long groupId, Date retriveTime) {
        List<ChatMsgEntity> chatMsgEntityList =  chatMsgMapper.getOfflineMessagesForUser(groupId, retriveTime);

    }
}
