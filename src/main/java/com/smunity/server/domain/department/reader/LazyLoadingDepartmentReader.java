package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 1. 반정규화 도입(#155) 전 방식. 조회 시마다 members 컬렉션을 로딩해 size()로 센다.
 * default_batch_fetch_size(#148) 설정이 있으면 학과 수만큼이 아니라 IN 절 배치로 로딩되지만,
 * 회원 엔티티 전체를 메모리에 올리는 비용은 그대로다.
 * 호출하는 쪽의 트랜잭션 안에서 실행되어야 한다. (지연 로딩)
 */
@RequiredArgsConstructor
public class LazyLoadingDepartmentReader implements DepartmentReader {

    private final DepartmentRepository departmentRepository;

    @Override
    public ListResponse<DepartmentResponse> readDepartments() {
        List<DepartmentResponse> responses = departmentRepository.findAll(Sort.by("id")).stream()
                .map(LazyLoadingDepartmentReader::toResponse)
                .toList();
        return ListResponse.from(responses);
    }

    private static DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .college(department.getCollege())
                .name(department.getName())
                .code(department.getCode())
                .count(department.getMembers().size())
                .build();
    }
}
