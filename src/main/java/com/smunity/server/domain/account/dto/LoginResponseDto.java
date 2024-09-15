package com.smunity.server.domain.account.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        Long memberId,
        String accessToken,
        String refreshToken
) {

    public static LoginResponseDto of(Long memberId, String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
