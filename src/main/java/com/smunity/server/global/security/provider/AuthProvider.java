package com.smunity.server.global.security.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * 인증(Authentication) 정보를 제공하는 Provider 인터페이스
 */
public interface AuthProvider {

    // HTTP 요청에서 인증(Authentication) 정보를 추출
    Authentication getAuthentication(HttpServletRequest request);
}
