package com.smunity.server.domain.member.dto;

import com.smunity.server.global.common.entity.enums.Exemption;

public record ChangeExemptionRequest(
        Exemption exemption
) {

}
