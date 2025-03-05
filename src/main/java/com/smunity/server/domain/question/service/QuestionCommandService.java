package com.smunity.server.domain.question.service;

import com.smunity.server.domain.question.dto.QuestionRequest;
import com.smunity.server.domain.question.dto.QuestionResponse;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.domain.question.mapper.QuestionMapper;
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

    public QuestionResponse createQuestion(Long memberId, QuestionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        Question question = QuestionMapper.INSTANCE.toEntity(request);
        question.setMember(member);
        return QuestionMapper.INSTANCE.toResponse(questionRepository.save(question));
    }

    public QuestionResponse updateQuestion(Long memberId, Boolean isAdmin, Long questionId, QuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        PermissionUtil.validatePermission(memberId, isAdmin, question.getMember().getId());
        question.update(request.title(), request.content(), request.anonymous());
        return QuestionMapper.INSTANCE.toResponse(question);
    }

    public void deleteQuestion(Long memberId, Boolean isAdmin, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new GeneralException(ErrorCode.QUESTION_NOT_FOUND));
        PermissionUtil.validatePermission(memberId, isAdmin, question.getMember().getId());
        questionRepository.delete(question);
    }
}
