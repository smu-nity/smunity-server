package com.smunity.server.domain.answer.service;

import com.smunity.server.domain.answer.dto.AnswerRequest;
import com.smunity.server.domain.answer.mapper.AnswerMapper;
import com.smunity.server.domain.answer.repository.AnswerRepository;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.domain.question.repository.QuestionRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerCommandServiceTest {

    private static final AnswerRequest REQUEST = new AnswerRequest("답변");

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private AnswerMapper answerMapper;
    @Mock
    private Member member;
    @Mock
    private Question question;
    @InjectMocks
    private AnswerCommandService service;

    @Test
    void 이미_답변이_있는_질문에_답변을_등록하면_충돌_예외가_발생한다() {
        stubFound();
        when(answerRepository.existsByQuestionId(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.createAnswer(1L, 1L, REQUEST))
                .isInstanceOf(GeneralException.class)
                .extracting(e -> ((GeneralException) e).getErrorCode())
                .isEqualTo(ErrorCode.ANSWER_CONFLICT);

        verify(answerRepository, never()).save(any());
    }

    private void stubFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
    }
}
