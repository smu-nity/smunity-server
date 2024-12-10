package com.smunity.server.domain.department.service;

import com.smunity.server.domain.department.dto.DepartmentResponseDto;
import com.smunity.server.domain.department.repository.DepartmentQueryRepository;
import com.smunity.server.global.common.dto.ListResponseDto;
import com.smunity.server.global.common.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentQueryRepository departmentQueryRepository;

    public ListResponseDto<DepartmentResponseDto> readDepartments(Boolean isEditable) {
        List<Department> departments = departmentQueryRepository.findByIsEditable(isEditable);
        List<DepartmentResponseDto> responseDtoList = DepartmentResponseDto.from(departments);
        return ListResponseDto.from(responseDtoList);
    }
}
