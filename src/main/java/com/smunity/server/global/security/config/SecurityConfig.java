package com.smunity.server.global.security.config;

import com.smunity.server.global.security.encoder.Pbkdf2PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정을 구성하는 클래스
 * HTTP 보안 설정, 인증 필터 추가 등 보안 관련 설정을 정의
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * PBKDF2 암호화 방식으로 PasswordEncoder 빈을 생성 (Django 기본 암호화 방식)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    /**
     * Spring Security의 필터 체인 설정 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // HTTP 응답 헤더 설정
        http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 경로별 인가 작업
        http.authorizeHttpRequests(authorize -> authorize
                // H2 콘솔과 Swagger UI 및 API 문서에 대한 접근 허용
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // API 계정 관련 요청에 대한 접근 허용
                .requestMatchers("/api/v1/accounts/**").permitAll()
                // 나머지 모든 요청은 인증 필요
                .anyRequest().authenticated()
        );

        return http.build();
    }
}
