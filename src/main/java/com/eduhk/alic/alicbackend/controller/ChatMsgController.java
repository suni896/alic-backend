package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.ChatMsgRespVO;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import com.eduhk.alic.alicbackend.model.vo.OfflineMsgVO;
import com.eduhk.alic.alicbackend.model.vo.ReconnectMsgVO;
import com.eduhk.alic.alicbackend.service.ChatMsgService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.eduhk.alic.alicbackend.common.constant.StompConstant.SUB_USER;

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
    @MessageMapping("/getOfflineMsg/{groupId}")
    public void getOfflineMessages(@DestinationVariable Long groupId, @Payload OfflineMsgVO offlineMsgVO) {
        log.info("离线消息groupId: "+groupId);
        log.info("离线消息: "+offlineMsgVO.toString());
        List<ChatMsgRespVO> offlineMessages = chatMsgService.getOfflineMessages(groupId, offlineMsgVO.getUserId(), offlineMsgVO.getPageNum(), offlineMsgVO.getPageSize());

        // 将离线消息推送到用户
        for (ChatMsgRespVO msg : offlineMessages) {
//            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/offline", msg);
            messagingTemplate.convertAndSendToUser(offlineMsgVO.getUserId().toString(), "/"+groupId+"/offline"+SUB_USER.getPath(), msg);
            log.info("/"+groupId+"/offline"+SUB_USER.getPath());
        }
    }

    @MessageMapping("/getReconnectMsg/{groupId}")
    public void getReconnectMessages(@DestinationVariable Long groupId, @Payload ReconnectMsgVO reconnectMsgVO) throws JsonProcessingException {
        List<ChatMsgRespVO> offlineMessages = chatMsgService.getReconnectOfflineMessages(groupId, reconnectMsgVO.getUserId(), reconnectMsgVO.getLastMsgId());

        // 将消息推送到用户
        for (ChatMsgRespVO msg : offlineMessages) {
            messagingTemplate.convertAndSendToUser(reconnectMsgVO.getUserId().toString(), "/"+groupId+"/reconnect"+SUB_USER.getPath(), msg);
            ObjectMapper mapper = new ObjectMapper();
            String messageJson = mapper.writeValueAsString(msg);
            chatMsgService.deleteMessageFromInbox(groupId, String.valueOf(reconnectMsgVO.getUserId()), messageJson);
        }

    }
}
