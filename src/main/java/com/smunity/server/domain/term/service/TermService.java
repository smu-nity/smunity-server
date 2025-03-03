package com.smunity.server.domain.term.service;

import com.smunity.server.domain.term.dto.TermResponseDto;
import com.smunity.server.domain.term.mapper.TermMapper;
import com.smunity.server.global.common.entity.Term;
import com.smunity.server.global.common.repository.TermRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public TermResponseDto readCurrentTerm() {
        Term term = termRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new GeneralException(ErrorCode.TERM_NOT_FOUND));
        return TermMapper.INSTANCE.toDto(term);
    }
}
