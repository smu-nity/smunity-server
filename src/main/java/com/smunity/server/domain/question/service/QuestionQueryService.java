package com.smunity.server.domain.question.service;

import com.smunity.server.domain.question.dto.QuestionResponseDto;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.domain.question.repository.QuestionRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionQueryService {

    private final QuestionRepository questionRepository;

    public Page<QuestionResponseDto> readQuestions(Pageable pageable) {
        Page<Question> questions = questionRepository.findAll(pageable);
        return QuestionResponseDto.from(questions);
    }

    public QuestionResponseDto readQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        return QuestionResponseDto.from(question);
    }
}
