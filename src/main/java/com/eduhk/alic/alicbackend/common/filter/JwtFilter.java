package com.eduhk.alic.alicbackend.common.filter;

import cn.hutool.jwt.Claims;
import com.eduhk.alic.alicbackend.utils.JwtUtils;
import com.eduhk.alic.alicbackend.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import javax.servlet.*;
import java.io.IOException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FuSu
 * @date 2025/1/20 11:50
 */
@Slf4j
@WebFilter(urlPatterns = "/v1/*")
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = ((HttpServletRequest) request).getRequestURI();
        log.info("url:{}",url);
//        for (String excludedUrl : excludedUrlList) {
//            if (Pattern.matches(excludedUrl.replace("*", ".*"), url)) {
//                isMatch = true;
//                break;
//            }
//        }
        if (url.contains("/auth") || url.contains("/swagger-ui")) {
            chain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //处理跨域问题，跨域的请求首先会发一个options类型的请求
        if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            chain.doFilter(httpServletRequest,httpServletResponse);
        }
//        String authHeader = httpServletRequest.getHeader("Authorization");
//        if (authHeader == null) {
//            log.info("不包含 token 放行 无验证，直接返回");
//            return;
//        }
//        String jwtToken = authHeader.substring(7);
        String jwtToken = "";
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    // 获取 JWT_TOKEN 的值
                    jwtToken = cookie.getValue();
                }
            }
        }

        log.info("jwtToken:{}",jwtToken);
//            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
//            RedisUtils redisService = (RedisUtils) factory.getBean("redisUtils");
//            String authorization = httpServletRequest.getHeader(GlobalConfig.TOKEN_NAME);
        boolean validate = JwtUtils.validate(jwtToken);
        if (!validate) {
            log.info("token 验证失败，直接返回");
            // 设置 HTTP 响应状态码，表示未授权
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // 直接返回响应，防止继续执行
            httpServletResponse.getWriter().write("Unauthorized access");
            httpServletResponse.getWriter().flush();
            return;
        }
        Long userId = JwtUtils.parseTokenUserId(jwtToken);
        String userIdStr = userId.toString();
        log.info("userId:{}",userIdStr);
        String token = RedisUtils.get(userIdStr);
        if (token == null || !token.equalsIgnoreCase(jwtToken)) {
            log.info("token 不存在或不匹配，直接返回");
            // 设置 HTTP 响应状态码，表示未授权
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // 直接返回响应，防止继续执行
            httpServletResponse.getWriter().write("Unauthorized access");
            httpServletResponse.getWriter().flush();
            return;
        }

        // 可以将用户信息放入请求中，供后续处理使用
        httpServletRequest.setAttribute("userId", userId);
        chain.doFilter(httpServletRequest, httpServletResponse);

    }

}
