package com.smunity.server.domain.department.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * 학과 조회 설정
 *
 * @param readStrategy 학과별 회원 수 조회 방식 (#342 부하 테스트용 전환 설정)
 * @param cache        캐시 설정 (REDIS_CACHE / LOCAL_CACHE)
 * @param scheduler    회원 수 갱신 스케줄러 설정 (DENORMALIZED)
 */
@ConfigurationProperties(prefix = "department")
public record DepartmentProperties(
        @DefaultValue("DENORMALIZED") ReadStrategy readStrategy,
        @DefaultValue Cache cache,
        @DefaultValue Scheduler scheduler
) {

    public enum ReadStrategy {
        DENORMALIZED,   // 0. 반정규화 컬럼(member_count) 읽기 + 스케줄러 갱신
        LAZY_LOADING,   // 1. 반정규화 도입 전 방식, members.size()
        GROUP_BY,       // 2. QueryDSL left join + group by 집계
        REDIS_CACHE,    // 3. GROUP_BY + Redis 캐시
        LOCAL_CACHE     // 3'. GROUP_BY + Caffeine 로컬 캐시
    }

    public record Cache(
            @DefaultValue("5m") Duration ttl
    ) {

    }

    public record Scheduler(
            @DefaultValue("true") boolean enabled
    ) {

    }
}
