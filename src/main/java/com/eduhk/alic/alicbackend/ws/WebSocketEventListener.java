package com.eduhk.alic.alicbackend.ws;

import com.eduhk.alic.alicbackend.common.constant.MessageTypeEnum;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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

    private static final String chatgroupConnectPrefix = "CHATGROUP_CONNECT_";

    private static final String userChatGroupConnectPrefix = "USER_CHAT_GROUP_CONNECT_";


    @SubscribeMapping("/topic/{groupId}")
    public void handleUserJoinChatRoom(@DestinationVariable String groupId, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().forEach((k, v)->log.info(v.toString()));
        String userId = (String) headerAccessor.getSessionAttributes().get("user");

        if (userId != null) {
            log.info("User subscribed to group " + groupId + ": " + userId);

            // 构造聊天室加入消息
            ChatMsgVO chatMessage = new ChatMsgVO();
            chatMessage.setType(MessageTypeEnum.JOIN);

            // 将用户添加到 Redis 的在线用户集合中
            RedisUtils.sAdd(chatgroupConnectPrefix + groupId, userId);
            RedisUtils.sAdd(userChatGroupConnectPrefix + userId, groupId);

            // 广播加入消息给所有用户
            messagingTemplate.convertAndSend("/app/chat/" + groupId, chatMessage);
        }
    }

    @SubscribeMapping("/{groupId}")
    public void handleUserJoinChatRoom1(@DestinationVariable String groupId, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().forEach((k, v)->log.info(v.toString()));
        String userId = (String) headerAccessor.getSessionAttributes().get("user");

        if (userId != null) {
            log.info("User subscribed to group " + groupId + ": " + userId);

            // 构造聊天室加入消息
            ChatMsgVO chatMessage = new ChatMsgVO();
            chatMessage.setType(MessageTypeEnum.JOIN);

            // 将用户添加到 Redis 的在线用户集合中
            RedisUtils.sAdd(chatgroupConnectPrefix + groupId, userId);
            RedisUtils.sAdd(userChatGroupConnectPrefix + userId, groupId);

            // 广播加入消息给所有用户
            messagingTemplate.convertAndSend("/app/chat/" + groupId, chatMessage);
        }
    }

//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        log.info("Received a new web socket connection");
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        //todo 下面两行，取userId&groupId 要根据客户端定义修改
//        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
//        String groupId = (String) headerAccessor.getSessionAttributes().get("groupId");
//
//        if(userId != null) {
//            log.info("User connected : " + userId);
//
//            ChatMsgVO chatMessage = new ChatMsgVO();
//            chatMessage.setType(MessageTypeEnum.JOIN);
//            //todo 保存登录状态
//            RedisUtils.sAdd(chatgroupConnectPrefix+groupId, userId);
//
//            //todo 进入聊天室 dest要改
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            log.info("User Disconnected : " + userId);

            ChatMsgVO chatMessage = new ChatMsgVO();
//            chatMessage.setType(MessageTypeEnum.LEAVE);
            RedisUtils.setMembers(chatgroupConnectPrefix+userId).forEach(
                    groupId -> {
                        RedisUtils.sRemove(chatgroupConnectPrefix+groupId, userId);
                        RedisUtils.sRemove(userChatGroupConnectPrefix+userId, groupId);
                    }
            );
            //todo 进入聊天室 dest要改
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}

