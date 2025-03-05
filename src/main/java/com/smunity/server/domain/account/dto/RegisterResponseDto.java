package com.smunity.server.domain.account.dto;

import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterResponseDto(
        String username,
        MemberRole role,
        LocalDateTime createdAt
) {

}
