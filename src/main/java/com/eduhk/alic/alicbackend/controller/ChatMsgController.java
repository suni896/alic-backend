package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.ChatMsgRespVO;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import com.eduhk.alic.alicbackend.service.ChatMsgService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/18 13:34
 */
@RestController
@Slf4j
public class ChatMsgController {
    @Resource
    private ChatMsgService chatMsgService;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{groupId}")
    public void sendMessage(@DestinationVariable Long groupId, @Payload ChatMsgVO message) throws JsonProcessingException {
        log.info("groupId: "+groupId);
        log.info(message.toString());

        Long infoId = chatMsgService.insertChatMsg(message);
        chatMsgService.sendMessageToOnlineMember(message, infoId);

    }

    // 离线消息拉取接口，用户登录后调用
    @RequestMapping("/getOfflineMessages")
    public List<ChatMsgRespVO> getOfflineMessages(Long userId, Long groupId, int pageNum, int pageSize) {
        List<ChatMsgRespVO> offlineMessages = chatMsgService.getOfflineMessages(groupId, userId, pageNum, pageSize);

        // 将离线消息推送到用户
        for (ChatMsgRespVO msg : offlineMessages) {
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/offline", msg);
        }
        return offlineMessages;
    }

    @RequestMapping("/getOfflineMessages")
    public List<ChatMsgRespVO> getReconnectMessages(Long userId, Long groupId, Long lastMsgId) {
        List<ChatMsgRespVO> offlineMessages = chatMsgService.getReconnectOfflineMessages(groupId, userId, lastMsgId);

        // 将消息推送到用户
        for (ChatMsgRespVO msg : offlineMessages) {
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/offline", msg);
        }
        return offlineMessages;
    }
}
