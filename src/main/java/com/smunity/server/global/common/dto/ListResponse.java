package com.smunity.server.global.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ListResponse<T>(
        int count,
        List<T> content
) {

    public static <T> ListResponse<T> from(List<T> responses) {
        return ListResponse.<T>builder()
                .count(responses.size())
                .content(responses)
                .build();
    }
}
