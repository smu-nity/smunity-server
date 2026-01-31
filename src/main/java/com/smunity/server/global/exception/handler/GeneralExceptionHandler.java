package com.smunity.server.global.exception.handler;

import com.smunity.exception.AuthClientException;
import com.smunity.exception.AuthServerException;
import com.smunity.exception.code.BaseCode;
import com.smunity.server.global.common.dto.ErrorResponse;
import com.smunity.server.global.common.service.SlackNotifier;
import com.smunity.server.global.exception.DepartmentNotFoundException;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionHandler {

    private final SlackNotifier slackNotifier;

    // 사용자 정의 예외(GeneralException) 처리 메서드
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleGeneralException(GeneralException ex) {
        return handleException(ex, false, ex.getErrorCode());
    }

    // 요청 파라미터 검증 실패(MethodArgumentNotValidException) 처리 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleException(ex, false, ErrorCode.VALIDATION_FAILED);
    }

    // 컨트롤러 메서드 파라미터의 유효성 검증 실패(HandlerMethodValidationException) 처리 메서드
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return handleException(ex, false, ErrorCode.BAD_REQUEST);
    }

    // 지원되지 않는 HTTP 메서드 요청(HttpRequestMethodNotSupportedException) 처리 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return handleException(ex, false, ErrorCode.METHOD_NOT_ALLOWED);
    }

    // 메서드 인자 타입 불일치(MethodArgumentTypeMismatchException) 처리 메서드
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return handleException(ex, false, ErrorCode.INVALID_ENUM_VALUE);
    }

    // 학생 인증 클라이언트 예외(AuthClientException) 처리 메서드
    @ExceptionHandler(AuthClientException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleAuthClientException(AuthClientException ex) {
        return handleException(ex, false, ex.getErrorCode());
    }

    // 학생 인증 서버 예외(AuthServerException) 처리 메서드
    @ExceptionHandler(AuthServerException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleAuthServerException(AuthServerException ex) {
        return handleException(ex, true, ex.getErrorCode());
    }

    // 학과명 도메인 불일치(DepartmentNotFoundException) 처리 메서드
    @ExceptionHandler(DepartmentNotFoundException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        return handleException(ex, true, ErrorCode.DEPARTMENT_SERVER_ERROR);
    }

    // 기타 모든 예외(Exception) 처리 메서드
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<Void>> handleGlobalException(Exception ex) {
        return handleException(ex, true, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // 예외 공통 로깅 및 알림 처리 메서드
    private void handleException(Exception ex, Boolean isError) {
        if (isError) {
            log.error("{} : {}", ex.getClass(), ex.getMessage(), ex);
            slackNotifier.sendMessage(ex);
        } else {
            log.warn("{} : {}", ex.getClass(), ex.getMessage());
        }
    }

    // 공통 예외 처리 메서드 (기본 HTTP 상태코드 사용)
    private ResponseEntity<ErrorResponse<Void>> handleException(Exception ex, Boolean isError, BaseCode code) {
        handleException(ex, isError);
        return ErrorResponse.handle(code);
    }

    // Validation 예외 처리 메서드 (BindException 전용)
    private ResponseEntity<ErrorResponse<Map<String, String>>> handleException(BindException ex, Boolean isError, BaseCode code) {
        handleException(ex, isError);
        return ErrorResponse.handle(code, ex.getFieldErrors());
    }
}
