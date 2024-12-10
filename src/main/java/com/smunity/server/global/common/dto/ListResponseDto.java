package com.smunity.server.global.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ListResponseDto<T>(
        int count,
        List<T> content
) {

    public static <T> ListResponseDto<T> from(List<T> responses) {
        return ListResponseDto.<T>builder()
                .count(responses.size())
                .content(responses)
                .build();
    }
}
