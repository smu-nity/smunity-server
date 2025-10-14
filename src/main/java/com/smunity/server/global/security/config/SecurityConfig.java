package com.smunity.server.global.security.config;

import com.smunity.server.global.security.config.encoder.Pbkdf2PasswordEncoder;
import com.smunity.server.global.security.filter.AuthenticationExceptionFilter;
import com.smunity.server.global.security.filter.AuthenticationFilter;
import com.smunity.server.global.security.handler.AccessDeniedHandlerImpl;
import com.smunity.server.global.security.handler.AuthenticationEntryPointImpl;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 구성
 * HTTP 보안 설정, 인증 필터 추가 등 보안 관련 설정을 정의
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * PBKDF2 암호화 방식으로 PasswordEncoder 빈을 생성 (Django 기본 암호화 방식)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    /**
     * Spring Security 필터 체인 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않도록 설정 (JWT 기반 인증 사용)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // H2 콘솔 접근을 위해 동일 출처로 Frame-Options 설정
        http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new AuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // 인증 실패 예외를 처리하는 필터를 AuthenticationFilter 앞에 추가
        http.addFilterBefore(new AuthenticationExceptionFilter(), AuthenticationFilter.class);

        // 인증 및 인가 실패 시 처리할 핸들러 설정
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
        );

        // 요청 경로별 접근 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                // 모든 사용자 접근 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**", "/actuator/**").permitAll()
                .requestMatchers("/api/v1/accounts/login", "/api/v1/accounts/refresh", "/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/terms/**", "/api/v1/departments", "/api/v1/members/count").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/questions/**").permitAll()

                // 재학생 인증 완료 사용자만 접근 허용 (ROLE_VERIFIED)
                .requestMatchers("/api/v1/accounts/register").hasRole("VERIFIED")

                // 관리자 권한 사용자만 접근 허용 (ROLE_ADMIN)
                .requestMatchers("/api/v1/members", "/api/v1/questions/{questionId}/answer").hasRole("ADMIN")

                // 그 외 요청은 인증된 사용자만 접근 허용 (ROLE_USER, ROLE_ADMIN)
                .anyRequest().authenticated()
        );

        return http.build();
    }
}
