package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.repository.DepartmentQueryRepository;
import com.smunity.server.global.common.dto.ListResponse;
import lombok.RequiredArgsConstructor;

/**
 * 2. QueryDSL left join + group by로 학과별 회원 수를 단일 쿼리로 집계한다.
 */
@RequiredArgsConstructor
public class GroupByDepartmentReader implements DepartmentReader {

    private final DepartmentQueryRepository departmentQueryRepository;

    @Override
    public ListResponse<DepartmentResponse> readDepartments() {
        return ListResponse.from(departmentQueryRepository.findAllWithMemberCount());
    }
}
