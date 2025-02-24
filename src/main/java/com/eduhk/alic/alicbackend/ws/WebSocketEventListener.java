package com.eduhk.alic.alicbackend.ws;

import com.eduhk.alic.alicbackend.common.constant.MessageTypeEnum;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Resource;

/**
 * @author FuSu
 * @date 2025/2/18 13:55
 */
@Slf4j
@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            log.info("User Disconnected : " + username);

            ChatMsgVO chatMessage = new ChatMsgVO();
            chatMessage.setType(MessageTypeEnum.JOIN);

            //todo 保存登录状态
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            log.info("User Disconnected : " + username);

            ChatMsgVO chatMessage = new ChatMsgVO();
            chatMessage.setType(MessageTypeEnum.LEAVE);

            //todo 删除登录状态
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}

