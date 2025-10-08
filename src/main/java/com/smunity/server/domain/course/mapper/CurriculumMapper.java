package com.smunity.server.domain.course.mapper;

import com.smunity.server.domain.course.dto.CultureResponse;
import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.SubDomain;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CurriculumMapper {

    default List<CultureResponse> toResponse(Member member, List<Curriculum> curriculums, Set<SubDomain> exemptions) {
        return curriculums.stream()
                .filter(curriculum -> !member.getSubDomain().equals(curriculum.getSubDomain()))
                .filter(curriculum -> !exemptions.contains(curriculum.getSubDomain()))
                .map(curriculum -> toResponse(member, curriculum.getSubDomain()))
                .toList();
    }

    default CultureResponse toResponse(Member member, SubDomain subDomain) {
        return CultureResponse.builder()
                .subDomain(subDomain)
                .subDomainName(subDomain.getName())
                .completed(member.checkCompleted(subDomain))
                .build();
    }
}
