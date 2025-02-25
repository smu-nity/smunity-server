package com.smunity.server.global.security.filter;

import com.smunity.server.global.security.exception.JwtAuthenticationException;
import com.smunity.server.global.security.exception.handler.JwtAuthenticationExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 예외를 핸들링하는 필터 클래스
 */
public class JwtAuthenticationExceptionFilter extends OncePerRequestFilter {

    /**
     * 요청을 필터링하고, JWT 인증 예외가 발생하면 오류 응답 설정
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            // 필터 체인 계속 실행
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException ex) {
            // 예외 발생 시 오류 응답 설정
            JwtAuthenticationExceptionHandler.handleException(response, ex, ex.getErrorCode());
        }
    }
}
