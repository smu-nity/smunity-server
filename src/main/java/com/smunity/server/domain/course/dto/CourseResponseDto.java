package com.smunity.server.domain.course.dto;

import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.Builder;

@Builder
public record CourseResponseDto(
        Long id,
        String year,
        String semester,
        String number,
        String name,
        String type,
        String domain,
        Category category,
        SubDomain subDomain,
        int credit
) {

}
