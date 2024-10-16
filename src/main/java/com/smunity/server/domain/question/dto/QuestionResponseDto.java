package com.smunity.server.domain.question.dto;

import com.smunity.server.domain.question.entity.Question;
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

    public static QuestionResponseDto from(Question question) {
        return QuestionResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .author(question.getAuthor())
                .answered(question.getAnswered())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}
