package com.smunity.server.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequest(
        @NotBlank(message = "내용은 필수 입력 항목 입니다.")
        String content
) {

}
