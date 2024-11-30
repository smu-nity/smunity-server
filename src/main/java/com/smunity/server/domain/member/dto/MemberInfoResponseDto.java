package com.smunity.server.domain.member.dto;

import com.smunity.server.global.common.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponseDto(
        String username,
        String name,
        String department,
        String email
) {

    public static MemberInfoResponseDto from(Member member) {
        return MemberInfoResponseDto.builder()
                .username(member.getUsername())
                .name(member.getName())
                .department(member.getDepartment().getName())
                .email(member.getEmail())
                .build();
    }
}
