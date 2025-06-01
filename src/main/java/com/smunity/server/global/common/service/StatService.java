package com.smunity.server.global.common.service;

import com.smunity.server.domain.question.repository.QuestionRepository;
import com.smunity.server.global.common.dto.StatResponseDto;
import com.smunity.server.global.common.mapper.StatMapper;
import com.smunity.server.global.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final StatMapper statMapper;

    public StatResponseDto getStatistics() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);
        long totalMembers = memberRepository.count();
        long newRegisters = memberRepository.countByCreatedAtBetween(yesterday, today);
        long unansweredQuestions = questionRepository.countByAnswerIsNull();
        return statMapper.toResponse(yesterday.toLocalDate(), totalMembers, newRegisters, unansweredQuestions);
    }
}
