package com.smunity.server.domain.member.dto;

import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
public record MemberInfoResponseDto(
        Long id,
        String username,
        MemberRole memberRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MemberInfoResponseDto from(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .memberRole(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public static Page<MemberInfoResponseDto> from(Page<Member> memberPage) {
        return memberPage.map(MemberInfoResponseDto::from);
    }
}
