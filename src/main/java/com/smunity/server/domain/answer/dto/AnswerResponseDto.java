package com.smunity.server.domain.answer.dto;

import com.smunity.server.domain.answer.entity.Answer;
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

    public static AnswerResponseDto from(Answer answer) {
        return AnswerResponseDto.builder()
                .id(answer.getId())
                .questionId(answer.getQuestion().getId())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
}
