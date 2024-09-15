package com.smunity.server.global.security.exception;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class JwtAuthenticationException extends GeneralException {

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
