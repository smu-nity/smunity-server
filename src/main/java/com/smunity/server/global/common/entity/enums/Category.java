package com.smunity.server.global.common.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {

    MAJOR_ADVANCED("1전심"),
    MAJOR_OPTIONAL("1전선"),
    CULTURE("교양"),
    ETC("기타");

    private final String name;

    public static Category findByName(String name) {
        return Arrays.stream(Category.values())
                .filter(category -> name.contains(category.getName()))
                .findFirst()
                .orElse(ETC);
    }
}
