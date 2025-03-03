package com.smunity.server.domain.question.mapper;

import com.smunity.server.domain.question.dto.QuestionReadResponseDto;
import com.smunity.server.domain.question.dto.QuestionRequestDto;
import com.smunity.server.domain.question.dto.QuestionResponseDto;
import com.smunity.server.domain.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionResponseDto toDto(Question question);

    @Mapping(target = "isAuthor", expression = "java(question.getIsAuthor(memberId))")
    QuestionReadResponseDto toDto(Question question, Long memberId);

    Question toEntity(QuestionRequestDto requestDto);

    default Page<QuestionReadResponseDto> toDto(Page<Question> questions, Long memberId) {
        return questions.map(question -> toDto(question, memberId));
    }
}
