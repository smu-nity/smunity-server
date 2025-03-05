package com.smunity.server.domain.term.mapper;

import com.smunity.server.domain.term.dto.TermResponseDto;
import com.smunity.server.global.common.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TermMapper {

    TermMapper INSTANCE = Mappers.getMapper(TermMapper.class);

    @Mapping(target = "semester", source = "term.semester.name")
    TermResponseDto toDto(Term term);
}
