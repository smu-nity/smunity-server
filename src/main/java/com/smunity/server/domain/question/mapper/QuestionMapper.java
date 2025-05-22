package com.smunity.server.domain.question.mapper;

import com.smunity.server.domain.question.dto.QuestionReadResponse;
import com.smunity.server.domain.question.dto.QuestionRequest;
import com.smunity.server.domain.question.dto.QuestionResponse;
import com.smunity.server.domain.question.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionResponse toResponse(Question question);

    @Mapping(target = "isAuthor", expression = "java(question.getIsAuthor(memberId))")
    QuestionReadResponse toResponse(Question question, Long memberId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "answer", ignore = true)
    Question toEntity(QuestionRequest request);

    default Page<QuestionReadResponse> toResponse(Page<Question> questions, Long memberId) {
        return questions.map(question -> toResponse(question, memberId));
    }
}
