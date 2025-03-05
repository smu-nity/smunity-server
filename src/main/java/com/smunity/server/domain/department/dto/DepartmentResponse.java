package com.smunity.server.domain.department.dto;

import lombok.Builder;

@Builder
public record DepartmentResponse(
        String college,
        String name,
        String code,
        int count
) {

}
