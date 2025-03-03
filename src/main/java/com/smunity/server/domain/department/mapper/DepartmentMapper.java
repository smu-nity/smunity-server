package com.smunity.server.domain.department.mapper;

import com.smunity.server.domain.department.dto.DepartmentEditResponseDto;
import com.smunity.server.domain.department.dto.DepartmentResponseDto;
import com.smunity.server.global.common.dto.ListResponseDto;
import com.smunity.server.global.common.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentResponseDto toDto(Department department);

    DepartmentEditResponseDto toEditDto(Department department);

    default ListResponseDto<DepartmentResponseDto> toResponse(List<Department> departments) {
        return ListResponseDto.from(toDto(departments));
    }

    default ListResponseDto<DepartmentEditResponseDto> toEditResponse(List<Department> departments) {
        return ListResponseDto.from(toEditDto(departments));
    }

    default List<DepartmentResponseDto> toDto(List<Department> departments) {
        return departments.stream()
                .map(this::toDto)
                .toList();
    }

    default List<DepartmentEditResponseDto> toEditDto(List<Department> departments) {
        return departments.stream()
                .map(this::toEditDto)
                .toList();
    }
}
