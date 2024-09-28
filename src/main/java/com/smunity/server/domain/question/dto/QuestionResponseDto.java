package com.smunity.server.domain.question.dto;

import com.smunity.server.domain.question.entity.Question;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

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

    public static List<QuestionResponseDto> from(List<Question> questions) {
        return questions.stream()
                .map(QuestionResponseDto::from)
                .toList();
    }

    public static Page<QuestionResponseDto> from(Page<Question> questions) {
        return questions.map(QuestionResponseDto::from);
    }
}
