package com.smunity.server.domain.question.service;

import com.smunity.server.domain.question.dto.QuestionReadResponse;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.domain.question.mapper.QuestionMapper;
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

    public Page<QuestionReadResponse> readQuestions(Long memberId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAll(pageable);
        return QuestionMapper.INSTANCE.toResponse(questions, memberId);
    }

    public QuestionReadResponse readQuestion(Long memberId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        return QuestionMapper.INSTANCE.toResponse(question, memberId);
    }
}
