package com.smunity.server.global.common.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum Category {

    MAJOR_ADVANCED("1전심"),
    MAJOR_OPTIONAL("1전선"),
    FIRST_MAJOR("1전공"),
    SECOND_MAJOR("2전공"),
    CULTURE("교양"),
    ETC("기타");

    private static final Map<String, Category> CATEGORY_MAP = Map.of(
            "교필", CULTURE,
            "교선", CULTURE,
            "1교직", MAJOR_OPTIONAL
    );

    private final String name;

    public static Category of(String name) {
        return CATEGORY_MAP.getOrDefault(name, Category.findByName(name));
    }

    private static Category findByName(String name) {
        return Arrays.stream(Category.values())
                .filter(category -> name.contains(category.getName()))
                .findFirst()
                .orElse(ETC);
    }
}
