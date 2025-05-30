package com.smunity.server.global.scheduler;

import com.smunity.server.domain.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberCountScheduler {

    private final DepartmentService departmentService;

    @Scheduled(cron = "0 0 * * * *")
    public void runUpdateMemberCount() {
        departmentService.updateMemberCount();
        log.info("Member count update completed at {}", LocalDateTime.now());
    }
}
