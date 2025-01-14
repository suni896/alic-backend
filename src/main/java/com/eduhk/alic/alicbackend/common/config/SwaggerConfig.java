package com.eduhk.alic.alicbackend.common.config;

import io.swagger.annotations.SwaggerDefinition;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;



/**
 * @author FuSu
 * @date 2025/1/14 10:44
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    // 初始化创建Swagger Api

    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                // 详细信息定制
                .apiInfo(apiInfo())
                .select()
                // 指定当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.eduhk.alic.alicbackend.controller"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    // 添加摘要信息
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                .title("Alic Backend API")
                .description("Swagger Documentation")
                .contact(new Contact("", null, null))
                .version("1.0.0")
                .build();
    }
}
