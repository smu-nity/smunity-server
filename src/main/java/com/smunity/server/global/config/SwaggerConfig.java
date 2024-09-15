package com.smunity.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info())
                .components(new Components().addSecuritySchemes("access-token", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("access-token"));
    }

    private Info info() {
        return new Info()
                .title("SMUNITY API")
                .description("상명대학교 졸업요건 검사 사이트 API 명세서")
                .version("v1.0.0");
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
