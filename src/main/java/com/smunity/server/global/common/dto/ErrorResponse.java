package com.smunity.server.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smunity.exception.code.AuthErrorCode;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record ErrorResponse<T>(
        @NonNull
        String code,

        @NonNull
        String message,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T detail
) {

    public static ResponseEntity<ErrorResponse<Void>> handle(ErrorCode errorCode) {
        return ResponseEntity.status(HttpStatus.valueOf(errorCode.getValue())).body(from(errorCode));
    }

    public static ResponseEntity<ErrorResponse<Void>> handle(AuthErrorCode errorCode) {
        return ResponseEntity.status(HttpStatus.valueOf(errorCode.getValue())).body(from(errorCode));
    }

    public static ResponseEntity<ErrorResponse<Map<String, String>>> handle(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return ResponseEntity.status(HttpStatus.valueOf(errorCode.getValue())).body(of(errorCode, fieldErrors));
    }

    public static <T> ErrorResponse<T> from(ErrorCode errorCode) {
        return new ErrorResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ErrorResponse<T> from(AuthErrorCode errorCode) {
        return new ErrorResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    private static ErrorResponse<Map<String, String>> of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return new ErrorResponse<>(errorCode.getCode(), errorCode.getMessage(), convertErrors(fieldErrors));
    }

    private static Map<String, String> convertErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    public String toJsonString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
