package com.smunity.server.domain.course.dto;

import lombok.Builder;

@Builder
public record CreditResponseDto(
        String username,
        String name,
        int total,
        int completed,
        int major,
        int culture,
        int etc,
        int required,
        int completion
) {

}
