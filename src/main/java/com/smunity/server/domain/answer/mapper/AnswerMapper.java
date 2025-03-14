package com.smunity.server.domain.answer.mapper;

import com.smunity.server.domain.answer.dto.AnswerRequest;
import com.smunity.server.domain.answer.dto.AnswerResponse;
import com.smunity.server.domain.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(target = "questionId", source = "question.id")
    AnswerResponse toResponse(Answer answer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "question", ignore = true)
    Answer toEntity(AnswerRequest request);
}
