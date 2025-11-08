package com.smunity.server.global.common.service;

import com.smunity.server.global.common.dto.StatResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Profile("!prod")
@Service
class SlackNotifierMock implements SlackNotifier {

    @Override
    public void sendMessage(Exception ex) {
        log.info("Skip Slack(ex): {}", ex.getMessage());
    }

    @Override
    public void sendMessage(StatResponseDto dto) {
        log.info("Skip Slack(stat): {}", dto);
    }
}
