package com.smunity.server.global.common.service;

import com.smunity.server.domain.account.repository.LoginStatusRepository;
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
    private final LoginStatusRepository loginStatusRepository;
    private final QuestionRepository questionRepository;
    private final StatMapper statMapper;

    public StatResponseDto getStatistics() {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.minusDays(1).atStartOfDay();
        LocalDateTime end = now.atStartOfDay();
        long totalMembers = memberRepository.count();
        long newRegisters = memberRepository.countByCreatedAtBetween(start, end);
        long activeMembers = loginStatusRepository.countDistinctMemberByLoginAtBetween(start, end);
        long unansweredQuestions = questionRepository.countByAnswerIsNull();
        return statMapper.toResponse(start.toLocalDate(), totalMembers, newRegisters, activeMembers, unansweredQuestions);
    }
}
