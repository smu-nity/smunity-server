package com.smunity.server.domain.culture.service;

import com.smunity.server.domain.culture.dto.CultureResponse;
import com.smunity.server.domain.culture.entity.Culture;
import com.smunity.server.domain.culture.mapper.CultureMapper;
import com.smunity.server.domain.culture.repository.CultureQueryRepository;
import com.smunity.server.global.common.dto.ListResponse;
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

    public ListResponse<CultureResponse> readCultures(SubDomain subDomain) {
        List<Culture> cultures = cultureQueryRepository.findBySubDomain(subDomain);
        return CultureMapper.INSTANCE.toResponse(cultures);
    }
}
