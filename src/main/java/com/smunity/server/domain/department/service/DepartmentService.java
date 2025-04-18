package com.smunity.server.domain.department.service;

import com.smunity.server.domain.department.dto.DepartmentEditResponse;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.mapper.DepartmentMapper;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public ListResponse<DepartmentResponse> readDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return DepartmentMapper.INSTANCE.toResponse(departments);
    }

    public ListResponse<DepartmentEditResponse> readEditableDepartments() {
        List<Department> departments = departmentRepository.findAllByIsEditable(true);
        return DepartmentMapper.INSTANCE.toEditResponse(departments);
    }
}
