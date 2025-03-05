package com.smunity.server.domain.question.mapper;

import com.smunity.server.domain.question.dto.QuestionReadResponse;
import com.smunity.server.domain.question.dto.QuestionRequest;
import com.smunity.server.domain.question.dto.QuestionResponse;
import com.smunity.server.domain.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionResponse toResponse(Question question);

    @Mapping(target = "isAuthor", expression = "java(question.getIsAuthor(memberId))")
    QuestionReadResponse toResponse(Question question, Long memberId);

    Question toEntity(QuestionRequest requestDto);

    default Page<QuestionReadResponse> toResponse(Page<Question> questions, Long memberId) {
        return questions.map(question -> toResponse(question, memberId));
    }
}
