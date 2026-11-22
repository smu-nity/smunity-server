package com.smunity.server.domain.department.repository;

import com.smunity.server.domain.department.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentQueryRepository {

    List<DepartmentResponse> findAllWithMemberCount();
}
