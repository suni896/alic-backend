package com.eduhk.alic.alicbackend.common.interceptor;

import com.eduhk.alic.alicbackend.utils.JwtUtils;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.List;
import java.util.Map;

/**
 * @author FuSu
 * @date 2025/2/26 17:17
 */
@Slf4j
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        String jwtToken = "";
//        HttpHeaders headers = request.getHeaders();
//        List<String> cookieList = headers.get(HttpHeaders.COOKIE);
//        if (cookieList != null) {
//            for (String cookie : cookieList) {
//                if (cookie.contains("JWT_TOKEN")) {
//                    jwtToken = cookie.split("JWT_TOKE=")[1].split(";")[0];  // 提取 JWT
//                }
//            }
//        }
        request.getHeaders().keySet().forEach(key -> {
            log.info("key:{}",key);
            log.info("value:{}",request.getHeaders().get(key));
        });
        String authHeader = request.getHeaders().getFirst("Authorization");
        log.info("authHeader:{}",authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);  // 提取JWT
            log.info("jwtToken:{}",jwtToken);
            boolean validate = JwtUtils.validate(jwtToken);

            if (!validate) {
                log.info("token 验证失败，直接返回");
                return false;
            }
            Long userId = JwtUtils.parseTokenUserId(jwtToken);
            String userIdStr = userId.toString();
            log.info("userId:{}",userIdStr);
            String token = RedisUtils.get(userIdStr);
            if (token == null || !token.equalsIgnoreCase(jwtToken)) {
                log.info("token 不存在或不匹配，直接返回");
                return false;
            }
            // 可以将用户信息放入请求中，供后续处理使用
            attributes.put("userId", userId);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
