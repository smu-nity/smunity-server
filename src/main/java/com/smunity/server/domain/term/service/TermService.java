package com.smunity.server.domain.term.service;

import com.smunity.server.domain.term.dto.TermResponse;
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
    private final TermMapper termMapper;

    public TermResponse readCurrentTerm() {
        Term term = termRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new GeneralException(ErrorCode.TERM_NOT_FOUND));
        return termMapper.toResponse(term);
    }
}
