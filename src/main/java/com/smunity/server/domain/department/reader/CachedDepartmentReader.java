package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.dto.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import static com.smunity.server.global.config.CacheConfig.DEPARTMENTS;

/**
 * 3 / 3'. 위임 대상(GROUP_BY)의 결과를 캐싱한다.
 * 캐시 저장소(Redis / Caffeine)는 CacheConfig의 CacheManager가 결정한다.
 * 캐시 장애 시에는 CacheErrorHandler가 예외를 삼키고 위임 대상(DB)으로 조회한다.
 */
@RequiredArgsConstructor
public class CachedDepartmentReader implements DepartmentReader {

    private final DepartmentReader delegate;

    @Override
    @Cacheable(cacheNames = DEPARTMENTS, key = "'all'")
    public ListResponse<DepartmentResponse> readDepartments() {
        return delegate.readDepartments();
    }
}
