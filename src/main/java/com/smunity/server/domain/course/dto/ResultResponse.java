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

}
