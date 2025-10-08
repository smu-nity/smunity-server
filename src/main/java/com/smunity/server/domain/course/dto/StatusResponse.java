package com.smunity.server.domain.course.dto;

import lombok.Builder;

import static com.smunity.server.domain.course.util.ProgressUtil.calculateCompletion;
import static com.smunity.server.domain.course.util.ProgressUtil.calculateRequired;

@Builder
public record StatusResponse(
        int total,
        int completed,
        int required,
        int completion
) {

    public static StatusResponse of(int total, int completed) {
        return StatusResponse.builder()
                .total(total)
                .completed(completed)
                .required(calculateRequired(total, completed))
                .completion(calculateCompletion(total, completed))
                .build();
    }
}
