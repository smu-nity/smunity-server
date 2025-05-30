package com.smunity.server.global.security.exception.handler;

import com.smunity.server.global.common.dto.ErrorResponse;
import com.smunity.server.global.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 인증·인가 예외를 처리하는 핸들러
 */
@Slf4j
public class AuthenticationExceptionHandler {

    /**
     * 인증·인가 예외 발생 시 오류 응답을 반환
     */
    public static void handleException(HttpServletResponse response, RuntimeException ex,
                                       ErrorCode errorCode) throws IOException {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());

        // 응답의 Content-Type, 문자 인코딩, 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getValue());

        // ErrorResponse 객체를 JSON 문자열로 변환하여 응답 본문에 작성
        ErrorResponse<Object> errorResponse = ErrorResponse.from(errorCode);
        response.getWriter().write(errorResponse.toJsonString());
    }
}
