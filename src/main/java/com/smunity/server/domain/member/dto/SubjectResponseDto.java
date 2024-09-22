package com.smunity.server.domain.member.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SubjectResponseDto<T>(
        int count,
        List<T> content
) {

    public static <T> SubjectResponseDto<T> from(List<T> responses) {
        return SubjectResponseDto.<T>builder()
                .count(responses.size())
                .content(responses)
                .build();
    }
}
