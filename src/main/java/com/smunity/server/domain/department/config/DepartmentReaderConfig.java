package com.smunity.server.domain.department.config;

import com.smunity.server.domain.department.mapper.DepartmentMapper;
import com.smunity.server.domain.department.reader.*;
import com.smunity.server.domain.department.repository.DepartmentQueryRepository;
import com.smunity.server.global.common.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Configuration
@EnableConfigurationProperties(DepartmentProperties.class)
public class DepartmentReaderConfig {

    /**
     * 트랜잭션은 캐시 안쪽(DB 조회 시점)에서 시작한다.
     * 캐시 히트 시에는 트랜잭션을 열지 않아 DB 커넥션을 점유하지 않는다.
     */
    @Bean
    public DepartmentReader departmentReader(DepartmentProperties properties,
                                             DepartmentRepository departmentRepository,
                                             DepartmentQueryRepository departmentQueryRepository,
                                             DepartmentMapper departmentMapper,
                                             PlatformTransactionManager transactionManager) {
        TransactionTemplate readOnly = new TransactionTemplate(transactionManager);
        readOnly.setReadOnly(true);
        log.info("[DepartmentReaderConfig] readStrategy={}", properties.readStrategy());
        return switch (properties.readStrategy()) {
            case DENORMALIZED ->
                    new TransactionalDepartmentReader(new DenormalizedDepartmentReader(departmentRepository, departmentMapper), readOnly);
            case LAZY_LOADING ->
                    new TransactionalDepartmentReader(new LazyLoadingDepartmentReader(departmentRepository), readOnly);
            case GROUP_BY ->
                    new TransactionalDepartmentReader(new GroupByDepartmentReader(departmentQueryRepository), readOnly);
            case REDIS_CACHE, LOCAL_CACHE -> new CachedDepartmentReader(
                    new TransactionalDepartmentReader(new GroupByDepartmentReader(departmentQueryRepository), readOnly));
        };
    }
}
