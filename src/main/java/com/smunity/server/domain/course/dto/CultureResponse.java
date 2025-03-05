package com.smunity.server.domain.course.dto;

import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.Builder;

@Builder
public record CultureResponse(
        SubDomain subDomain,
        String subDomainName,
        boolean completed
) {

}
