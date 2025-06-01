package com.smunity.server.global.scheduler;

import com.smunity.server.global.common.dto.StatResponseDto;
import com.smunity.server.global.common.service.StatService;
import com.smunity.server.global.common.util.SlackUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatSendScheduler {

    private final StatService statService;
    private final SlackUtil slackUtil;

    @Scheduled(cron = "0 0 0 * * *")
    public void runUpdateMemberCount() {
        StatResponseDto responseDto = statService.getStatistics();
        slackUtil.sendMessage(responseDto);
        log.info("Statistics sent successfully at {} : {}", LocalDateTime.now(), responseDto);
    }
}
