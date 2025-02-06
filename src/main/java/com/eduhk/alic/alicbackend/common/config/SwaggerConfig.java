package com.eduhk.alic.alicbackend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.service.*;
import java.util.Collections;
import java.util.List;


/**
 * @author FuSu
 * @date 2025/1/14 10:44
 */
@Configuration
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

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
//                .components(new Components().addSecuritySchemes("Bearer Token",
//                        new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")));
//    }
}

//@Configuration
//public class SwaggerConfig {
//
//    public Docket createRestApi(String groupName, String basePackage) {
//        return new Docket(DocumentationType.OAS_30)
//                //添加token的参数
//                .securityContexts(mySecurityContexts())
//                .securitySchemes(mySecuritySchemes())
//                .apiInfo(apiInfo()).groupName(groupName)
//                // true 启用Swagger3.0， false 禁用（生产环境要禁用）
//                .enable(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.eduhk.alic.alicbackend.controller"))
//                .build();
//    }
//
//    /**
//     * 这里设置 swagger 认证的安全上下文
//     */
//    public List<SecurityContext> mySecurityContexts() {
//        return Collections.singletonList(SecurityContext.builder()
//                .securityReferences(Collections.singletonList(SecurityReference.builder()
//                        .reference("Authorization")
//                        .scopes(new AuthorizationScope[]{new AuthorizationScope("global",
//                                "accessEverything")}).build())).build());
//    }
//
//    public List<SecurityScheme> mySecuritySchemes() {
//        //注意，这里应对应登录token鉴权对应的k-v
//        return Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));
//    }
//
//
//    /**
//     * API 页面上半部分展示信息
//     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Swagger3接口文档")
//                .description("heeiya-app接口文档")
//                .contact(new Contact("", "", ""))
//                .version("1.0")
//                .build();
//    }
//
////    private List<RequestParameter> getGlobalRequestParameters() {
////        List<RequestParameter> parameters = new ArrayList<>();
////        parameters.add(new RequestParameterBuilder().name("lang").description("国际化标识,如：中文 zh_cn,英文  en_us").in(ParameterType.HEADER).build());
////        parameters.add(new RequestParameterBuilder().name("Api-Version").description("版本").in(ParameterType.HEADER).build());
////        return parameters;
////    }
//
//
//    @Bean
//    public Docket account() {
//        return createRestApi("账号中心", "com.xxx.xxx.controller");
//    }


//}