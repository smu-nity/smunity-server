package com.smunity.server.domain.department.repository;

import com.smunity.server.global.common.entity.Department;

import java.util.List;

public interface DepartmentQueryRepository {

    List<Department> findByIsEditable(Boolean isEditable);
}
