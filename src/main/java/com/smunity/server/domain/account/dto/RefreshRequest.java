package com.smunity.server.domain.account.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "리프레시 토큰 입력은 필수 입니다.")
        String refreshToken
) {

}
