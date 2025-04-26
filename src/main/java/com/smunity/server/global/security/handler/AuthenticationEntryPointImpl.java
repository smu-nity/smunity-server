package com.smunity.server.global.security.handler;

import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.exception.handler.AuthenticationExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 인증(Authentication) 예외를 처리하는 EntryPoint
 * 인증이 필요한 요청에 인증 정보가 없거나 유효하지 않을 때 호출
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    /**
     * 인증 실패 시 호출되는 메서드
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        AuthenticationExceptionHandler.handleException(response, ex, ErrorCode.UNAUTHORIZED_EXCEPTION);
    }
}
