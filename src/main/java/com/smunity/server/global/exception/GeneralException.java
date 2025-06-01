package com.smunity.server.global.exception;

import com.smunity.server.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return "[%s] %s".formatted(errorCode.getCode(), errorCode.getMessage());
    }
}
