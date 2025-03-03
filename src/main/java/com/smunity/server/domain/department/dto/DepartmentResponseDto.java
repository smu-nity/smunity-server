package com.smunity.server.domain.department.dto;

import lombok.Builder;

@Builder
public record DepartmentResponseDto(
        String college,
        String name,
        String code,
        int count
) {

}
