package com.smunity.server.domain.department.dto;

import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.Builder;

import java.util.List;

@Builder
public record DepartmentResponseDto(
        Long id,
        String college,
        String name,
        SubDomain subDomain,
        boolean isEditable,
        String code
) {

    public static DepartmentResponseDto from(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .college(department.getCollege())
                .name(department.getName())
                .subDomain(department.getSubDomain())
                .isEditable(department.isEditable())
                .code(department.getCode())
                .build();
    }


    public static List<DepartmentResponseDto> from(List<Department> departments) {
        return departments.stream().map(DepartmentResponseDto::from).toList();
    }
}
