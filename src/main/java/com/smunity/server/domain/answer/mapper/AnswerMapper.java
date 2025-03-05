package com.smunity.server.domain.answer.mapper;

import com.smunity.server.domain.answer.dto.AnswerRequestDto;
import com.smunity.server.domain.answer.dto.AnswerResponseDto;
import com.smunity.server.domain.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(target = "questionId", source = "question.id")
    AnswerResponseDto toDto(Answer answer);

    Answer toEntity(AnswerRequestDto requestDto);
}
