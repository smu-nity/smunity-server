package com.smunity.server.global.security.handler;

import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.exception.handler.AuthenticationExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * JWT 인증 실패 시 오류 응답을 보내는 EntryPoint 클래스
 * JWT 인증 시 인증 정보가 없거나 잘못된 경우
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
