package com.smunity.server.domain.answer.dto;

import com.smunity.server.domain.answer.entity.Answer;
import jakarta.validation.constraints.NotBlank;

public record AnswerRequestDto(
        @NotBlank(message = "내용은 필수 입력 항목 입니다.")
        String content
) {

    public Answer toEntity() {
        return Answer.builder()
                .content(content)
                .build();
    }
}
