package com.smunity.server.global.exception.handler;

import com.smunity.exception.AuthException;
import com.smunity.server.global.common.dto.ErrorResponse;
import com.smunity.server.global.common.service.SlackService;
import com.smunity.server.global.exception.DepartmentNotFoundException;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionHandler {

    private final SlackService slackService;

    // 사용자 정의 예외(GeneralException) 처리 메서드
    @ExceptionHandler(GeneralException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleGeneralException(GeneralException ex) {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ex.getErrorCode());
    }

    // 학생 인증 예외(AuthException) 처리 메서드
    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleAuthException(AuthException ex) {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ex.getErrorCode());
    }

    // 요청 파라미터 검증 실패(MethodArgumentNotValidException) 처리 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.VALIDATION_FAILED, ex.getFieldErrors());
    }

    // 컨트롤러 메서드 파라미터의 유효성 검증 실패(HandlerMethodValidationException) 처리 메서드 - @PermissionCheckValidator
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(extractErrorCode(ex));
    }

    // 지원되지 않는 HTTP 메서드 요청(HttpRequestMethodNotSupportedException) 처리 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.METHOD_NOT_ALLOWED);
    }

    // 메서드 인자 타입 불일치(MethodArgumentTypeMismatchException) 처리 메서드
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("{} : {}", ex.getClass(), ex.getMessage());
        return ErrorResponse.handle(ErrorCode.INVALID_ENUM_VALUE);
    }

    // 학과명 도메인 불일치(DepartmentNotFoundException) 처리 메서드
    @ExceptionHandler(DepartmentNotFoundException.class)
    protected ResponseEntity<ErrorResponse<Void>> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        log.error("{} : {}", ex.getClass(), ex.getMessage(), ex);
        slackService.sendMessage(ex);
        return ErrorResponse.handle(ErrorCode.DEPARTMENT_SERVER_ERROR);
    }

    // 기타 모든 예외(Exception) 처리 메서드
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<Void>> handleException(Exception ex) {
        log.error("{} : {}", ex.getClass(), ex.getMessage(), ex);
        slackService.sendMessage(ex);
        return ErrorResponse.handle(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // HandlerMethodValidationException 에서 ErrorCode 를 추출하는 메서드
    private ErrorCode extractErrorCode(HandlerMethodValidationException ex) {
        return ex.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .map(ErrorCode::valueOf)
                .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
    }
}
