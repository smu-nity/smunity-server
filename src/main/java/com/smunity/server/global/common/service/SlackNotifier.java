package com.smunity.server.global.common.service;

import com.smunity.server.global.common.dto.StatResponseDto;

public interface SlackNotifier {

    void sendMessage(Exception ex);

    void sendMessage(StatResponseDto dto);
}
