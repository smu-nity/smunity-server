package com.smunity.server.domain.question.dto;

import com.smunity.server.domain.question.entity.Question;
import jakarta.validation.constraints.NotBlank;

public record QuestionRequestDto(
        @NotBlank(message = "제목 입력은 필수 입니다.")
        String title,
        @NotBlank(message = "내용은 필수 입력 항목 입니다.")
        String content,
        boolean anonymous
) {

    public Question toEntity() {
        return Question.builder()
                .title(title)
                .content(content)
                .anonymous(anonymous)
                .build();
    }
}
