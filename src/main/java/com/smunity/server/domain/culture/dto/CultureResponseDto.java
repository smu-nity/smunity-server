package com.smunity.server.domain.culture.dto;

import lombok.Builder;

@Builder
public record CultureResponseDto(
        Long id,
        String number,
        String name,
        String type,
        int credit
) {

}
