package com.smunity.server.global.common.dto;

public record StatResponseDto(
        long totalMembers,
        long newRegisters,
        long activeMembers,
        long unansweredQuestions
) {

    @Override
    public String toString() {
        return """
                - 총 회원 수: %d명
                - 신규 가입자 수: %d명
                - 활성 사용자(DAU): %d명
                - 미답변 질문 수: %d개
                """.formatted(totalMembers, newRegisters, activeMembers, unansweredQuestions);
    }
}
