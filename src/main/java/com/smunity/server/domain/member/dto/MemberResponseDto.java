package com.smunity.server.domain.member.dto;

import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.MemberRole;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
public record MemberResponseDto(
        Long id,
        String username,
        String name,
        String department,
        String email,
        MemberRole memberRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .department(member.getDepartment().getName())
                .email(member.getEmail())
                .memberRole(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public static Page<MemberResponseDto> from(Page<Member> memberPage) {
        return memberPage.map(MemberResponseDto::from);
    }
}
