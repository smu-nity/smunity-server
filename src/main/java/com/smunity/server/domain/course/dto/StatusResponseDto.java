package com.smunity.server.domain.course.dto;

import lombok.Builder;

@Builder
public record StatusResponseDto(
        int total,
        int completed,
        int required,
        int completion
) {

}
