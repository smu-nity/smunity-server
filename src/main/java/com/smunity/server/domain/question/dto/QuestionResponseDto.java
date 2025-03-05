package com.smunity.server.domain.question.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QuestionResponseDto(
        Long id,
        String title,
        String content,
        String author,
        boolean answered,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
