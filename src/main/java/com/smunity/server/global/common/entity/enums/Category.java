package com.smunity.server.global.common.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    private static final Map<String, Category> CULTURE_MAP = Map.of("교필", CULTURE, "교선", CULTURE, "1교직", MAJOR_OPTIONAL);
    private static final Map<String, Category> SINGLE_MAJOR_MAP = Map.of("1전심", MAJOR_ADVANCED, "1전선", MAJOR_OPTIONAL);
    private static final Map<String, Category> DOUBLE_MAJOR_MAP = Map.of("1전심", FIRST_MAJOR, "1전선", FIRST_MAJOR, "2전심", SECOND_MAJOR, "2전선", SECOND_MAJOR);

    private final String name;

    public static Category of(String name, boolean isDoubleMajor) {
        return CULTURE_MAP.getOrDefault(name, getMajorCategory(name, isDoubleMajor));
    }

    public static Category getMajorCategory(Category category) {
        return (category == FIRST_MAJOR || category == SECOND_MAJOR) ? null : category;
    }

    private static Category getMajorCategory(String name, boolean isDoubleMajor) {
        return (isDoubleMajor ? DOUBLE_MAJOR_MAP : SINGLE_MAJOR_MAP).getOrDefault(name, ETC);
    }
}
