package com.smunity.server.domain.account.dto;

import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;

@Builder
public record LoginResponseDto(
        Long memberId,
        MemberRole memberRole,
        String accessToken,
        String refreshToken
) {

    public static LoginResponseDto of(Long memberId, MemberRole memberRole, String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
                .memberId(memberId)
                .memberRole(memberRole)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
