package com.smunity.server.domain.department.dto;

import com.smunity.server.global.common.dto.ListResponseDto;
import com.smunity.server.global.common.entity.Department;
import lombok.Builder;

import java.util.List;

@Builder
public record DepartmentResponseDto(
        String college,
        String name,
        String code,
        int count
) {

    public static ListResponseDto<DepartmentResponseDto> from(List<Department> departments) {
        return ListResponseDto.from(toDtoList(departments));
    }

    public static List<DepartmentResponseDto> toDtoList(List<Department> departments) {
        return departments.stream().map(DepartmentResponseDto::from).toList();
    }

    private static DepartmentResponseDto from(Department department) {
        return DepartmentResponseDto.builder()
                .college(department.getCollege())
                .name(department.getName())
                .code(department.getCode())
                .count(department.getMemberCount())
                .build();
    }
}
