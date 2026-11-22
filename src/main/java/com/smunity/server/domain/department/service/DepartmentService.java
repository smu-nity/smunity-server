package com.smunity.server.domain.department.service;

import com.smunity.server.domain.department.dto.DepartmentEditResponse;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.mapper.DepartmentMapper;
import com.smunity.server.domain.department.reader.DepartmentReader;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.repository.DepartmentRepository;
import com.smunity.server.global.exception.DepartmentNotFoundException;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentReader departmentReader;

    // 트랜잭션은 DepartmentReader 내부(캐시 안쪽)에서 시작한다. (DepartmentReaderConfig)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ListResponse<DepartmentResponse> readDepartments() {
        return departmentReader.readDepartments();
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

    @Transactional
    public void updateMemberCount() {
        departmentRepository.findAll()
                .forEach(Department::updateMemberCount);
    }
}
