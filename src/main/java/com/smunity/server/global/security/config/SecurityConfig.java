package com.smunity.server.global.security.config;

import com.smunity.server.global.security.config.encoder.Pbkdf2PasswordEncoder;
import com.smunity.server.global.security.filter.JwtAuthenticationExceptionFilter;
import com.smunity.server.global.security.filter.JwtAuthenticationFilter;
import com.smunity.server.global.security.handler.JwtAccessDeniedHandler;
import com.smunity.server.global.security.handler.JwtAuthenticationEntryPoint;
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
 * Spring Security 설정을 구성하는 클래스
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
     * Spring Security 의 formLogin 필터 체인 설정 구성
     */
    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        // Swagger UI 및 API 문서 경로, 로그인 페이지에 대한 보안 설정
        http.securityMatcher("/swagger-ui/**", "/v3/api-docs/**", "/login");

        // 로그인 성공 시 Swagger UI 로 이동
        http.formLogin(authorize -> authorize
                .defaultSuccessUrl("/swagger-ui/index.html")
                .permitAll()
        );

        // 경로별 인가 작업
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()
                .anyRequest().permitAll()
        );

        return http.build();
    }

    /**
     * Spring Security 의 JWT 필터 체인 설정 구성
     */
    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 세션 사용 안함
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // HTTP 응답 헤더 설정
        http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // JWT 기반 인증을 처리하기 위해 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이전에 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // JWT 인증 예외 핸들링 필터 추가
        http.addFilterBefore(new JwtAuthenticationExceptionFilter(), JwtAuthenticationFilter.class);

        // 인증 및 인가 오류 핸들러 추가
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
        );

        // 경로별 인가 작업
        http.authorizeHttpRequests(authorize -> authorize
                // H2 콘솔, Actuator 에 대한 접근 허용
                .requestMatchers("/h2-console/**", "/actuator/prometheus").permitAll()

                // 재학생 인증을 완료한 사용자 (ROLE_VERIFIED)
                .requestMatchers("/api/v1/accounts/register").hasRole("VERIFIED")

                // 모든 사용자
                .requestMatchers("/api/v1/accounts/**", "/api/v1/auth/**", "/api/v1/departments", "/api/v1/members/count").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/questions/**").permitAll()

                // 관리자 권한을 가진 사용자 (ROLE_ADMIN)
                .requestMatchers("/api/v1/members", "/api/v1/questions/{questionId}/answer").hasRole("ADMIN")

                // 인증된 사용자 (ROLE_USER, ROLE_ADMIN)
                .anyRequest().authenticated()
        );

        return http.build();
    }
}
