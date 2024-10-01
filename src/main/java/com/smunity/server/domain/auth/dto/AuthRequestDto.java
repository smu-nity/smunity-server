package com.smunity.server.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank(message = "학번 입력은 필수 입니다.")
        String username,

        @NotBlank(message = "비밀번호 입력은 필수 입니다.")
        String password
) {

}
