package com.smunity.server.global.scheduler;

import com.smunity.server.domain.department.config.DepartmentProperties;
import com.smunity.server.domain.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.smunity.server.domain.department.config.DepartmentProperties.ReadStrategy.DENORMALIZED;
import static com.smunity.server.global.common.logging.Loggers.event;

@Component
@RequiredArgsConstructor
public class MemberCountScheduler {

    private final DepartmentService departmentService;
    private final DepartmentProperties departmentProperties;

    /**
     * 반정규화 컬럼(member_count)은 DENORMALIZED 방식에서만 사용하므로, 다른 방식에서는 갱신하지 않는다.
     */
    @Scheduled(cron = "${department.scheduler.cron:0 0 * * * *}")
    public void updateMemberCount() {
        if (departmentProperties.readStrategy() != DENORMALIZED || !departmentProperties.scheduler().enabled()) {
            return;
        }
        long start = System.nanoTime();
        departmentService.updateMemberCount();
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        event.info("[MemberCountScheduler] event=updateMemberCount elapsedMs={}", elapsedMs);
    }
}
