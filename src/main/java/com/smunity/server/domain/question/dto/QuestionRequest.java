package com.smunity.server.domain.question.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionRequest(
        @NotBlank(message = "제목 입력은 필수 입니다.")
        String title,

        @NotBlank(message = "내용은 필수 입력 항목 입니다.")
        String content,

        boolean anonymous
) {

}
