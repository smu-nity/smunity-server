package com.smunity.server.domain.course.dto;

import lombok.Builder;

@Builder
public record StatusResponse(
        int total,
        int completed,
        int required,
        int completion
) {

}
