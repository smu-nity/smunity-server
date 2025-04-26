package com.smunity.server.global.security.filter;

import com.smunity.server.global.security.exception.JwtAuthenticationException;
import com.smunity.server.global.security.exception.handler.AuthenticationExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 인증 예외를 처리하는 필터
 */
public class AuthenticationExceptionFilter extends OncePerRequestFilter {

    /**
     * 요청을 필터링하고, JWT 인증 예외가 발생하면 오류 응답을 반환
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException ex) {
            // 인증 예외 발생 시 오류 응답 처리
            AuthenticationExceptionHandler.handleException(response, ex, ex.getErrorCode());
        }
    }
}
