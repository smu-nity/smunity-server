package com.smunity.server.domain.member.dto;

import com.smunity.server.global.common.entity.enums.Exemption;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
        String username,
        String name,
        String department,
        String deptCode,
        boolean deptEditable,
        Long yearId,
        Exemption exemption
) {

}
