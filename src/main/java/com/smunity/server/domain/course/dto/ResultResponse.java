package com.smunity.server.domain.course.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ResultResponse<T>(
        boolean completed,
        StatusResponse status,
        int count,
        List<T> content
) {

    public static <T> ResultResponse<T> of(int total, int completed, List<T> responses) {
        return ResultResponse.<T>builder()
                .completed(total <= completed)
                .status(StatusResponse.of(total, completed))
                .count(responses.size())
                .content(responses)
                .build();
    }
}
