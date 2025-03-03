package com.smunity.server.domain.course.mapper;

import com.smunity.server.domain.course.dto.CultureResponseDto;
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

    default List<CultureResponseDto> toDto(List<Curriculum> curriculums, Set<SubDomain> exemptions, Member member) {
        return curriculums.stream()
                .filter(curriculum -> !member.getSubDomain().equals(curriculum.getSubDomain()))
                .filter(curriculum -> !exemptions.contains(curriculum.getSubDomain()))
                .map(curriculum -> toDto(curriculum.getSubDomain(), member))
                .toList();
    }

    default CultureResponseDto toDto(SubDomain subDomain, Member member) {
        return CultureResponseDto.builder()
                .subDomain(subDomain)
                .subDomainName(subDomain.getName())
                .completed(member.checkCompleted(subDomain))
                .build();
    }
}
