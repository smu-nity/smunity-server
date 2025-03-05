package com.smunity.server.domain.answer.service;

import com.smunity.server.domain.answer.dto.AnswerRequestDto;
import com.smunity.server.domain.answer.dto.AnswerResponseDto;
import com.smunity.server.domain.answer.entity.Answer;
import com.smunity.server.domain.answer.mapper.AnswerMapper;
import com.smunity.server.domain.answer.repository.AnswerRepository;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.domain.question.repository.QuestionRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerCommandService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public AnswerResponseDto createAnswer(Long memberId, Long questionId, AnswerRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        Answer answer = AnswerMapper.INSTANCE.toEntity(requestDto);
        answer.setData(member, question);
        return AnswerMapper.INSTANCE.toDto(answerRepository.save(answer));
    }

    public AnswerResponseDto updateAnswer(Long memberId, Long questionId, AnswerRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        Answer answer = answerRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.ANSWER_NOT_FOUND));
        answer.update(member, requestDto.content());
        return AnswerMapper.INSTANCE.toDto(answer);
    }

    public void deleteAnswer(Long questionId) {
        Answer answer = answerRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.ANSWER_NOT_FOUND));
        answerRepository.delete(answer);
    }
}
