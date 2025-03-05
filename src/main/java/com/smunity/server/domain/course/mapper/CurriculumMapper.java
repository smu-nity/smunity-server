package com.smunity.server.domain.course.mapper;

import com.smunity.server.domain.course.dto.CultureResponse;
import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.SubDomain;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface CurriculumMapper {

    CurriculumMapper INSTANCE = Mappers.getMapper(CurriculumMapper.class);

    default List<CultureResponse> toResponse(List<Curriculum> curriculums, Set<SubDomain> exemptions, Member member) {
        return curriculums.stream()
                .filter(curriculum -> !member.getSubDomain().equals(curriculum.getSubDomain()))
                .filter(curriculum -> !exemptions.contains(curriculum.getSubDomain()))
                .map(curriculum -> toResponse(curriculum.getSubDomain(), member))
                .toList();
    }

    default CultureResponse toResponse(SubDomain subDomain, Member member) {
        return CultureResponse.builder()
                .subDomain(subDomain)
                .subDomainName(subDomain.getName())
                .completed(member.checkCompleted(subDomain))
                .build();
    }
}
