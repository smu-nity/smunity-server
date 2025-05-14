package com.smunity.server.domain.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "학번 입력은 필수 입니다.")
        @Pattern(regexp = "^\\d{9}$", message = "학번은 9자리 숫자 이어야 합니다.")
        String username,

        @NotBlank(message = "비밀번호 입력은 필수 입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자리 이어야 합니다.")
        String password,

        @NotBlank(message = "이메일 입력은 필수 입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        String email,

        @NotBlank(message = "이름 입력은 필수 입니다.")
        String name,

        @NotBlank(message = "학과 입력은 필수 입니다.")
        String department,

        String secondDepartment
) {

}
