package com.smunity.server.domain.member.dto;

import lombok.Builder;

@Builder
public record MemberCountDto(
        long count
) {

    public static MemberCountDto of(long count) {
        return MemberCountDto.builder()
                .count(count)
                .build();
    }
}
