package com.smunity.server.domain.course.dto;

import com.smunity.server.global.common.entity.Member;
import lombok.Builder;

import static com.smunity.server.domain.course.dto.StatusResponseDto.calculateCompletion;
import static com.smunity.server.domain.course.dto.StatusResponseDto.calculateRequired;
import static com.smunity.server.global.common.entity.enums.Category.*;

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

    public static CreditResponseDto from(int total, Member member) {
        int completed = member.getCompletedCredits();
        int major = member.getCompletedCredits(MAJOR_ADVANCED) + member.getCompletedCredits(MAJOR_OPTIONAL);
        int culture = member.getCompletedCredits(CULTURE);
        return CreditResponseDto.builder()
                .username(member.getUsername())
                .name(member.getName())
                .total(total)
                .completed(completed)
                .major(major)
                .culture(culture)
                .etc(completed - major - culture)
                .required(calculateRequired(total, completed))
                .completion(calculateCompletion(total, completed))
                .build();
    }
}
