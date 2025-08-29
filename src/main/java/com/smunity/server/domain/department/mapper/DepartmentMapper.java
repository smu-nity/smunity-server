package com.smunity.server.domain.department.mapper;

import com.smunity.server.domain.department.dto.DepartmentEditResponse;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "count", expression = "java(department.getMembers().size())")
    DepartmentResponse toResponse(Department department);

    @Mapping(target = "isEditable", ignore = true)
    DepartmentEditResponse toEditResponse(Department department);

    default ListResponse<DepartmentResponse> toResponse(List<Department> departments) {
        return ListResponse.from(toListResponse(departments));
    }

    default ListResponse<DepartmentEditResponse> toEditResponse(List<Department> departments) {
        return ListResponse.from(toEditListResponse(departments));
    }

    default List<DepartmentResponse> toListResponse(List<Department> departments) {
        return departments.stream()
                .map(this::toResponse)
                .toList();
    }

    default List<DepartmentEditResponse> toEditListResponse(List<Department> departments) {
        return departments.stream()
                .map(this::toEditResponse)
                .toList();
    }
}
