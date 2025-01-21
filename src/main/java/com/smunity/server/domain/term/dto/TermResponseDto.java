package com.smunity.server.domain.term.dto;

import com.smunity.server.global.common.entity.Term;
import lombok.Builder;

@Builder
public record TermResponseDto(
        Long id,
        int year,
        String semester
) {

    public static TermResponseDto from(Term term) {
        return TermResponseDto.builder()
                .id(term.getId())
                .year(term.getYear())
                .semester(term.getSemester().getName())
                .build();
    }
}
