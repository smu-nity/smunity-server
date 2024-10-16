package com.smunity.server.domain.question.dto;

import com.smunity.server.domain.question.entity.Question;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
public record QuestionReadResponseDto(
        Long id,
        String title,
        String content,
        String author,
        boolean isAuthor,
        boolean answered,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static QuestionReadResponseDto of(Long memberId, Question question) {
        return QuestionReadResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .author(question.getAuthor())
                .isAuthor(question.getIsAuthor(memberId))
                .answered(question.getAnswered())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }

    public static Page<QuestionReadResponseDto> of(Long memberId, Page<Question> questions) {
        return questions.map(question -> of(memberId, question));
    }
}
