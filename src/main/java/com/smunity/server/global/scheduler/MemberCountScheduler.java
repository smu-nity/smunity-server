package com.smunity.server.global.scheduler;

import com.smunity.server.domain.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.smunity.server.global.common.logging.Loggers.event;

@Component
@RequiredArgsConstructor
public class MemberCountScheduler {

    private final DepartmentService departmentService;

    @Scheduled(cron = "0 0 * * * *")
    public void updateMemberCount() {
        departmentService.updateMemberCount();
        event.info("[MemberCountScheduler] event=updateMemberCount status=success");
    }
}
