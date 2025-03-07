package com.eduhk.alic.alicbackend.common.interceptor;

import com.eduhk.alic.alicbackend.utils.JwtUtils;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FuSu
 * @date 2025/3/6 11:15
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入拦截器: url = {}", request.getRequestURI());
        String url = request.getRequestURI();
        log.info("url:{}", url);

        // 放行指定路径
        if (url.contains("/auth") || url.contains("/swagger-ui")) {
            return true;
        }

        // 处理跨域的 preflight OPTIONS 请求
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            log.info("OPTIONS 请求放行");
            return true;
        }

        // 获取 Cookie 中的 JWT_TOKEN
        String jwtToken = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    log.info("cookie:{}", jwtToken);
                }
            }
        }

        log.info("jwtToken:{}", jwtToken);

        // 验证 JWT
        boolean validate = JwtUtils.validate(jwtToken);
        if (!validate) {
            log.info("token 验证失败，直接返回");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access");
            response.getWriter().flush();
            return false;
        }

        Long userId = JwtUtils.parseTokenUserId(jwtToken);
        log.info("userId:{}", userId);

        // 验证 Redis 中是否存储了 token
        String token = RedisUtils.get(userId.toString());
        if (token == null || !token.equalsIgnoreCase(jwtToken)) {
            log.info("token 不存在或不匹配，直接返回");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access");
            response.getWriter().flush();
            return false;
        }

        // 存入请求作用域
        request.setAttribute("userId", userId);

        return true;
    }
}
