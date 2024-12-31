package com.smunity.server.global.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "swagger")
@Data
public class SwaggerProperties {

    private String username;
    private String password;
}
