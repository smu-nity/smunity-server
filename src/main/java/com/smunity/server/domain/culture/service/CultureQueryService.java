package com.smunity.server.domain.culture.service;

import com.smunity.server.domain.culture.dto.CultureResponseDto;
import com.smunity.server.domain.culture.entity.Culture;
import com.smunity.server.domain.culture.repository.CultureQueryRepository;
import com.smunity.server.domain.member.dto.SubjectResponseDto;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CultureQueryService {

    private final CultureQueryRepository cultureQueryRepository;

    public SubjectResponseDto<CultureResponseDto> getCultures(SubDomain subDomain) {
        List<Culture> cultures = cultureQueryRepository.findBySubDomain(subDomain);
        List<CultureResponseDto> responseDtoList = CultureResponseDto.from(cultures);
        return SubjectResponseDto.from(responseDtoList);
    }
}
