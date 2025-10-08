package com.smunity.server.domain.course.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProgressUtil {

    public static int calculateRequired(int total, int completed) {
        return Math.max(0, total - completed);
    }

    public static int calculateCompletion(int total, int completed) {
        return total != 0 ? Math.min(100, completed * 100 / total) : 100;
    }
}
