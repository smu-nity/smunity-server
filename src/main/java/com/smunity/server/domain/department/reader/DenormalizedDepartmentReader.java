package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.mapper.DepartmentMapper;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * 0. 반정규화 컬럼(member_count)을 읽는다. 값은 MemberCountScheduler가 갱신한다.
 */
@RequiredArgsConstructor
public class DenormalizedDepartmentReader implements DepartmentReader {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public ListResponse<DepartmentResponse> readDepartments() {
        return departmentMapper.toResponse(departmentRepository.findAll(Sort.by("id")));
    }
}
