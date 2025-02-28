package com.smunity.server.domain.answer.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnswerResponseDto(
        Long id,
        Long questionId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
