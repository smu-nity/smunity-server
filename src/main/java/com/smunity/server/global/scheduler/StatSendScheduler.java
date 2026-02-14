package com.smunity.server.global.scheduler;

import com.smunity.server.global.common.dto.StatResponseDto;
import com.smunity.server.global.common.service.SlackNotifier;
import com.smunity.server.global.common.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.smunity.server.global.common.logging.Loggers.event;

@Component
@RequiredArgsConstructor
public class StatSendScheduler {

    private final StatService statService;
    private final SlackNotifier slackNotifier;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendStatistics() {
        StatResponseDto responseDto = statService.getStatistics();
        slackNotifier.sendMessage(responseDto);
        event.info("[StatSendScheduler] event=sendStatistics payload={}", responseDto);
    }
}
