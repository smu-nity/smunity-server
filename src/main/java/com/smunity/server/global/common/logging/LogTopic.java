package com.smunity.server.global.common.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogTopic {

    EVENT("event");

    private final String name;
}
