package com.smunity.server.domain.account.dto;

import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterResponseDto(
        Long id,
        String username,
        MemberRole role,
        LocalDateTime createdAt
) {

    public static RegisterResponseDto from(Member member) {
        return RegisterResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
