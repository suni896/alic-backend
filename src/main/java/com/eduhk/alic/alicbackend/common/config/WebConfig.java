package com.eduhk.alic.alicbackend.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 支持方法
                .allowedHeaders("*")
                .exposedHeaders("*");
//        log.info("CORS configuration applied successfully!");
    }

}
