package com.smunity.server.domain.question.service;

import com.smunity.server.domain.question.dto.QuestionRequestDto;
import com.smunity.server.domain.question.dto.QuestionResponseDto;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.domain.question.repository.QuestionRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.util.PermissionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionCommandService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    public QuestionResponseDto createQuestion(Long memberId, QuestionRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        Question question = requestDto.toEntity();
        question.setMember(member);
        return QuestionResponseDto.from(questionRepository.save(question));
    }

    public QuestionResponseDto updateQuestion(Long memberId, Boolean isAdmin, Long questionId, QuestionRequestDto requestDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        PermissionUtil.validatePermission(memberId, isAdmin, question.getMember().getId());
        question.update(requestDto.title(), requestDto.content(), requestDto.anonymous());
        return QuestionResponseDto.from(question);
    }

    public void deleteQuestion(Long memberId, Boolean isAdmin, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        PermissionUtil.validatePermission(memberId, isAdmin, question.getMember().getId());
        questionRepository.delete(question);
    }
}
