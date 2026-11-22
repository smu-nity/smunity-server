package com.smunity.server.domain.department.service;

import com.smunity.server.domain.department.dto.DepartmentEditResponse;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.mapper.DepartmentMapper;
import com.smunity.server.domain.department.repository.DepartmentQueryRepository;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.repository.DepartmentRepository;
import com.smunity.server.global.exception.DepartmentNotFoundException;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.smunity.server.global.config.CacheConfig.DEPARTMENTS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentQueryRepository departmentQueryRepository;
    private final DepartmentMapper departmentMapper;

    @Cacheable(cacheNames = DEPARTMENTS, key = "'all'")
    public ListResponse<DepartmentResponse> readDepartments() {
        return ListResponse.from(departmentQueryRepository.findAllWithMemberCount());
    }

    public ListResponse<DepartmentEditResponse> readEditableDepartments() {
        List<Department> departments = departmentRepository.findAllByIsEditable(true);
        return departmentMapper.toEditResponse(departments);
    }

    public Department findDepartmentByName(String departmentName) {
        return departmentName != null ? departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentName)) : null;
    }

    public Department findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new GeneralException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }
}
