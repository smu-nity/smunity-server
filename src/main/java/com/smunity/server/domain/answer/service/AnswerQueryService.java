package com.smunity.server.domain.answer.service;

import com.smunity.server.domain.answer.dto.AnswerResponseDto;
import com.smunity.server.domain.answer.entity.Answer;
import com.smunity.server.domain.answer.repository.AnswerRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerQueryService {

    private final AnswerRepository answerRepository;

    public AnswerResponseDto readAnswer(Long questionId) {
        Answer answer = answerRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.ANSWER_NOT_FOUND));
        return AnswerResponseDto.from(answer);
    }
}