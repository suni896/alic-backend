package com.eduhk.alic.alicbackend.common.config;

import com.eduhk.alic.alicbackend.common.interceptor.JwtInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/22 15:06
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/v1/**") // 仅拦截 /v1/ 下的请求
                .excludePathPatterns("/auth/**", "/swagger-ui/**"); // 放行的路径
    }
//    @Resource
//    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口
                .allowCredentials(true) // 是否发送 Cookie
                .allowedOriginPatterns("http://localhost:5173","http://20.2.211.58","http://20.2.211.58:8080","http://20.2.211.58","https://20.2.211.58") // 支持域
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // 支持方法
                .allowedHeaders("*")
                .exposedHeaders("*");
//        log.info("CORS configuration applied successfully!");
    }

}
