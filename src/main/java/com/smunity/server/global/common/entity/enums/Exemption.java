package com.smunity.server.global.common.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exemption {

    FOREIGN("외국인특별전형"),
    DISABLED("장애학생"),
    TRANSFER("편입생");

    private final String description;
}
