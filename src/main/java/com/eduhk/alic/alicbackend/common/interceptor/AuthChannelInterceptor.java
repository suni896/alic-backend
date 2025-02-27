package com.eduhk.alic.alicbackend.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.eduhk.alic.alicbackend.utils.JwtUtils;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/27 11:54
 */
@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {
    /**
     * 连接前监听
     *
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //1、判断是否首次连接
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            //2、判断token
            List<String> nativeHeader = accessor.getNativeHeader("Authorization");
            nativeHeader.forEach(log::info);
            if (nativeHeader != null && !nativeHeader.isEmpty() && nativeHeader.get(0).startsWith("Bearer ")) {
                String jwtToken = nativeHeader.get(0).substring(7);  // 提取JWT
                log.info("jwtToken:{}",jwtToken);
                boolean validate = JwtUtils.validate(jwtToken);

                if (!validate) {
                    log.info("token 验证失败，直接返回");
                    return null;
                }
                Long userId = JwtUtils.parseTokenUserId(jwtToken);
                String userIdStr = userId.toString();
                log.info("userId:{}",userIdStr);
                String token = RedisUtils.get(userIdStr);
                if (token == null || !token.equalsIgnoreCase(jwtToken)) {
                    log.info("token 不存在或不匹配，直接返回");
                    return null;
                }
                Principal principal = new Principal() {
                    @Override
                    public String getName() {
                        return userIdStr;
                    }
                };
                accessor.setUser(principal);
                log.info(accessor.getUser().getName());
                return message;
            }
            return null;
        }
        //不是首次连接，已经登陆成功
        return message;
        }

}
