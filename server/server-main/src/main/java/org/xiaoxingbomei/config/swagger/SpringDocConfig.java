package org.xiaoxingbomei.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 配置类
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("后端脚手架 API 文档")
                        .version("1.0.0")
                        .description("基于SpringBoot + SpringCloud的后端脚手架项目API文档")
                        .contact(new Contact()
                                .name("xiaoxingbomei")
                                .email("your-email@example.com")
                                .url("https://github.com/xiaoxingbomei"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
} 