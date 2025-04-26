package com.smunity.server.global.security.filter;

import com.smunity.server.global.security.provider.AuthProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 인증을 처리하는 필터
 */
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthProvider authProvider;

    /**
     * 요청을 필터링하고, 인증 정보를 설정
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // HTTP 요청에서 인증 정보를 추출하고 검증
        Authentication authentication = authProvider.getAuthentication(request);

        // 인증 정보를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
