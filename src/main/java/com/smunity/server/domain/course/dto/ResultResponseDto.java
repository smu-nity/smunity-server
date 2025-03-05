package com.smunity.server.domain.course.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ResultResponseDto<T>(
        boolean completed,
        StatusResponseDto status,
        int count,
        List<T> content
) {

}
