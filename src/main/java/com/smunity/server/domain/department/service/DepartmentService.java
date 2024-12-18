package com.smunity.server.domain.department.service;

import com.smunity.server.domain.department.dto.DepartmentEditResponseDto;
import com.smunity.server.domain.department.dto.DepartmentResponseDto;
import com.smunity.server.global.common.dto.ListResponseDto;
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

    public ListResponseDto<DepartmentResponseDto> readDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return DepartmentResponseDto.from(departments);
    }

    public ListResponseDto<DepartmentEditResponseDto> readEditableDepartments() {
        List<Department> departments = departmentRepository.findAllByIsEditable(true);
        return DepartmentEditResponseDto.from(departments);
    }
}
