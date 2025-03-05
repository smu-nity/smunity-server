package com.smunity.server.domain.department.dto;

import lombok.Builder;

@Builder
public record DepartmentEditResponse(
        Long id,
        String name,
        boolean isEditable
) {

}
