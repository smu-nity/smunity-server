package com.smunity.server.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@RequiredArgsConstructor
public class InMemoryUserDetailsManagerConfig {

    private final SwaggerProperties swaggerProperties;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(createUserDetails());
    }

    private UserDetails createUserDetails() {
        return User.builder()
                .username(swaggerProperties.getUsername())
                .password(passwordEncoder.encode(swaggerProperties.getPassword()))
                .build();
    }
}
