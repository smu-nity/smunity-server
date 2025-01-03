package com.smunity.server.domain.account.dto;

import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;

@Builder
public record LoginResponseDto(
        String username,
        MemberRole memberRole,
        String accessToken,
        String refreshToken
) {

    public static LoginResponseDto of(String username, MemberRole memberRole, String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
                .username(username)
                .memberRole(memberRole)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
