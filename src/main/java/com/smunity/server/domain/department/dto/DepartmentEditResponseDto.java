package com.smunity.server.domain.department.dto;

import lombok.Builder;

@Builder
public record DepartmentEditResponseDto(
        Long id,
        String name,
        boolean isEditable
) {

}
