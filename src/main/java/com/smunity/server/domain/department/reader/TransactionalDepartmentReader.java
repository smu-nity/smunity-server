package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 위임 대상을 읽기 전용 트랜잭션 안에서 실행한다. (LAZY_LOADING의 지연 로딩 포함)
 */
@RequiredArgsConstructor
public class TransactionalDepartmentReader implements DepartmentReader {

    private final DepartmentReader delegate;
    private final TransactionTemplate transactionTemplate;

    @Override
    public ListResponse<DepartmentResponse> readDepartments() {
        return transactionTemplate.execute(status -> delegate.readDepartments());
    }
}
