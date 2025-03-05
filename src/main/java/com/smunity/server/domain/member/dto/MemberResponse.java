package com.smunity.server.domain.member.dto;

import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberResponse(
        Long id,
        String username,
        String name,
        String department,
        String email,
        MemberRole memberRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
