package com.smunity.server.domain.department.mapper;

import com.smunity.server.domain.department.dto.DepartmentEditResponseDto;
import com.smunity.server.domain.department.dto.DepartmentResponseDto;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "count", source = "memberCount")
    DepartmentResponseDto toDto(Department department);

    DepartmentEditResponseDto toEditDto(Department department);

    default ListResponse<DepartmentResponseDto> toResponse(List<Department> departments) {
        return ListResponse.from(toDto(departments));
    }

    default ListResponse<DepartmentEditResponseDto> toEditResponse(List<Department> departments) {
        return ListResponse.from(toEditDto(departments));
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
