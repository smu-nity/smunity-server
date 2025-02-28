package com.smunity.server.domain.account.dto;

import com.smunity.server.domain.account.entity.LoginStatus;
import com.smunity.server.domain.account.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "학번 입력은 필수 입니다.")
        String username,

        @NotBlank(message = "비밀번호 입력은 필수 입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자리 이어야 합니다.")
        String password
) {

    public LoginStatus toEntity(HttpServletRequest request) {
        return LoginStatus.builder()
                .ipAddress(IpUtil.getClientIp(request))
                .build();
    }
}
