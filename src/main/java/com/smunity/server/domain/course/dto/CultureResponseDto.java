package com.smunity.server.domain.course.dto;

import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record CultureResponseDto(
        SubDomain subDomain,
        String subDomainName,
        boolean completed
) {

    private static CultureResponseDto of(SubDomain subDomain, Member member) {
        return CultureResponseDto.builder()
                .subDomain(subDomain)
                .subDomainName(subDomain.getName())
                .completed(member.checkCompleted(subDomain))
                .build();
    }

    public static List<CultureResponseDto> of(List<Curriculum> curriculums, Set<SubDomain> exemptions, Member member) {
        return curriculums.stream()
                .filter(curriculum -> !member.getSubDomain().equals(curriculum.getSubDomain()))
                .filter(curriculum -> !exemptions.contains(curriculum.getSubDomain()))
                .map(curriculum -> of(curriculum.getSubDomain(), member))
                .toList();
    }
}
