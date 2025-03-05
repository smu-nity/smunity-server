package com.smunity.server.domain.major.dto;

import lombok.Builder;

@Builder
public record MajorResponseDto(
        Long id,
        String grade,
        String semester,
        String number,
        String name,
        String type,
        int credit
) {

}
