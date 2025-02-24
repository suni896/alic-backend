package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.ChatMsgRespVO;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import com.eduhk.alic.alicbackend.service.ChatMsgService;
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
    @SendTo("/topic/group/{groupId}")
    public ChatMsgRespVO sendMessage(@DestinationVariable Long groupId, @Payload ChatMsgVO message) {
        log.info("groupId: "+groupId);
        log.info(message.toString());
         //TODO 持久化消息
        chatMsgService.insertChatMsg(message);
        //加入队列，进行消息推送
        ChatMsgRespVO respVO = new ChatMsgRespVO();
        respVO.setMsgType(message.getMsgType());
        respVO.setGroupId(groupId);
        respVO.setSenderId(message.getSenderId());
        respVO.setContent(message.getContent());
        respVO.setCreateTime(message.getCreateTime());
        return respVO;
    }

    // 离线消息拉取接口，用户登录后调用
    @RequestMapping("/getOfflineMessages")
    public List<ChatMsgRespVO> getOfflineMessages(Long userId) {
        Date retriveTime = new Date(); // 假设用户登录时间为2023-01-01 00:00:00
        List<ChatMsgRespVO> offlineMessages = chatMsgService.getOfflineMessagesForUser(userId, retriveTime);

        // 将离线消息推送到用户
        for (ChatMsgRespVO msg : offlineMessages) {
            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/offline", msg);
        }
        return offlineMessages;
    }
}
