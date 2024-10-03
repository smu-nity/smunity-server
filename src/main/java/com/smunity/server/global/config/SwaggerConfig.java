package com.smunity.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final GitProperties gitProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info())
                .components(new Components().addSecuritySchemes("access-token", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("access-token"));
    }

    private Info info() {
        String version = gitProperties.get("build.version");
        String commit = gitProperties.get("commit.id.abbrev");
        return new Info()
                .title("SMUNITY API")
                .description("상명대학교 졸업요건 검사 사이트 API 명세서")
                .version("%s (%s)".formatted(version, commit));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
