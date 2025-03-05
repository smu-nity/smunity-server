package com.smunity.server.domain.term.dto;

import lombok.Builder;

@Builder
public record TermResponse(
        Long id,
        int year,
        String semester
) {

}
