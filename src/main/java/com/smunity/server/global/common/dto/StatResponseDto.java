package com.smunity.server.global.common.dto;

import java.time.LocalDate;

public record StatResponseDto(
        LocalDate date,
        long totalMembers,
        long newRegisters,
        long activeMembers,
        long unansweredQuestions
) {

    @Override
    public String toString() {
        return """
                %s
                - 총 회원 수: %d명
                - 신규 가입자 수: %d명
                - 활성 사용자(DAU): %d명
                - 미답변 질문 수: %d개
                """.formatted(date, totalMembers, newRegisters, activeMembers, unansweredQuestions);
    }
}
