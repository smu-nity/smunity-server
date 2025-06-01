package com.smunity.server.global.common.dto;

import java.time.LocalDate;

public record StatResponseDto(
        LocalDate date,
        long totalMembers,
        long newRegisters,
        long unansweredQuestions
) {

    public String toSlackMessage() {
        return """
                %s
                - 총 회원 수: %d명
                - 신규 가입자 수: %d명
                - 미답변 질문 수: %d개
                """.formatted(date, totalMembers, newRegisters, unansweredQuestions);
    }
}
