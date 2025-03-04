package com.eduhk.alic.alicbackend.ws;

import com.eduhk.alic.alicbackend.common.interceptor.AuthChannelInterceptor;
import com.eduhk.alic.alicbackend.common.interceptor.AuthHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.eduhk.alic.alicbackend.common.constant.StompConstant.*;

/**
 * @author FuSu
 * @date 2025/2/18 13:30
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(STOMP_ENDPOINT.getPath())
                .setAllowedOriginPatterns("*")
//                .addInterceptors(new AuthHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(STOMP_TOPIC.getPath(), STOMP_USER.getPath()); // 订阅前缀
        registry.setApplicationDestinationPrefixes(STOMP_APP.getPath());  // 发送消息前缀
        registry.setUserDestinationPrefix(STOMP_USER.getPath());
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new AuthChannelInterceptor());
    }

}