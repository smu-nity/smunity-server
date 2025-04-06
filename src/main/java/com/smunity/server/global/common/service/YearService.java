package com.smunity.server.global.common.service;

import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.repository.YearRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class YearService {

    private final YearRepository yearRepository;

    public Year findByUsername(String username) {
        int year = Integer.parseInt(username.substring(0, 4));
        return (year >= 2017 ? yearRepository.findByValue(year) : yearRepository.findById(1L))
                .orElseThrow(() -> new GeneralException(ErrorCode.YEAR_NOT_FOUND));
    }
}
