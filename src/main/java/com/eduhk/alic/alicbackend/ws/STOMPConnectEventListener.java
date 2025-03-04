package com.eduhk.alic.alicbackend.ws;

import com.eduhk.alic.alicbackend.common.constant.MessageTypeEnum;
import com.eduhk.alic.alicbackend.model.vo.ChatMsgVO;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

/**
 * @author FuSu
 * @date 2025/2/27 15:09
 */
@Component
@Slf4j
public class STOMPConnectEventListener implements ApplicationListener<AbstractSubProtocolEvent> {

    private static final String chatgroupConnectPrefix = "CHATGROUP_CONNECT_";

    private static final String userChatGroupConnectPrefix = "USER_CHAT_GROUP_CONNECT_";
    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String userId = event.getUser().getName();
        // 判断客户端的连接状态
        switch (sha.getCommand()) {
            case CONNECT:
                log.info("上线");
                break;
            case DISCONNECT:
                log.info("下线");
                if (userId != null) {
                    RedisUtils.setMembers(userChatGroupConnectPrefix + userId).forEach(
                            groupId ->{
                                RedisUtils.sRemove(chatgroupConnectPrefix + groupId, userId);
                                RedisUtils.sRemove(userChatGroupConnectPrefix + userId, groupId);
                                }
                            );
                }
                break;
            case SUBSCRIBE:
                log.info("订阅");
                log.info("订阅的频道："+sha.getDestination());
                log.info(event.getUser().getName());
                // 订阅的频道
                String groupId = getGroupIdFromPath(sha.getDestination());
                // 订阅的用户
                if (userId != null) {
                    log.info("User subscribed to group " + groupId + ": " + userId);

                    // 构造聊天室加入消息
                    ChatMsgVO chatMessage = new ChatMsgVO();
                    chatMessage.setType(MessageTypeEnum.JOIN);

                    // 将用户添加到 Redis 的在线用户集合中
                    RedisUtils.sAdd(chatgroupConnectPrefix + groupId, userId);
                    RedisUtils.sAdd(userChatGroupConnectPrefix + userId, groupId);
                }
                break;
//            case SEND:
//                log.info("发送");
//                break;
            case UNSUBSCRIBE:
                log.info("取消订阅");
                String groupId2 = getGroupIdFromPath(sha.getDestination());
                // 订阅的用户
                if (userId != null) {
                    log.info("User unsubscribed to group " + groupId2 + ": " + userId);

                    // 构造聊天室加入消息
                    ChatMsgVO chatMessage = new ChatMsgVO();
                    chatMessage.setType(MessageTypeEnum.JOIN);

                    // 将用户添加到 Redis 的在线用户集合中
                    RedisUtils.sRemove(chatgroupConnectPrefix + groupId2, userId);
                    RedisUtils.sRemove(userChatGroupConnectPrefix + userId, groupId2);
                }
                break;
//            case ACK:
//                log.info("确认");
//                sha.getAck();
//                break;
            default:
                break;
        }
    }

    private String getGroupIdFromPath(String path) {
        String[] split = path.split("/");
        if (path.contains("topic")) {
            return split[split.length - 1];
        }else if (path.contains("user")){
            return split[split.length - 2];
        }
        return null;

    }
}