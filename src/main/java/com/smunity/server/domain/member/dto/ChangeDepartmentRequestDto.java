package com.smunity.server.domain.member.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeDepartmentRequestDto(
        @NotNull(message = "학과 입력은 필수 입니다.")
        Long departmentId
) {

}
