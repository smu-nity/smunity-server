package com.smunity.server.domain.account.dto;

import com.smunity.server.global.common.entity.enums.MemberRole;

public record LoginResponse(
        String username,
        MemberRole memberRole,
        String accessToken,
        String refreshToken
) {

}
