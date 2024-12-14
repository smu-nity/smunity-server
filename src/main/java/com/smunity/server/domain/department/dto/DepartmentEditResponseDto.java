package com.smunity.server.domain.department.dto;

import com.smunity.server.global.common.entity.Department;
import lombok.Builder;

import java.util.List;

@Builder
public record DepartmentEditResponseDto(
        Long id,
        String name
) {

    public static DepartmentEditResponseDto from(Department department) {
        return DepartmentEditResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }

    public static List<DepartmentEditResponseDto> from(List<Department> departments) {
        return departments.stream().map(DepartmentEditResponseDto::from).toList();
    }
}
