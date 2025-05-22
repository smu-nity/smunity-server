package com.smunity.server.domain.term.mapper;

import com.smunity.server.domain.term.dto.TermResponse;
import com.smunity.server.global.common.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermMapper {

    @Mapping(target = "semester", source = "term.semester.name")
    TermResponse toResponse(Term term);
}
