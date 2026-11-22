package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;

/**
 * 학과 목록(학과별 회원 수 포함) 조회 방식
 * department.read-strategy 설정에 따라 구현체 하나가 등록된다. (DepartmentReaderConfig)
 */
public interface DepartmentReader {

    ListResponse<DepartmentResponse> readDepartments();
}
