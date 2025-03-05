package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.dto.CultureResponse;
import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.mapper.CurriculumMapper;
import com.smunity.server.domain.course.repository.CurriculumRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Exemption;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.smunity.server.global.common.entity.enums.SubDomain.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;

    public List<CultureResponse> readCurriculums(Member member, Domain domain) {
        List<Curriculum> curriculums = curriculumRepository.findAllByYearAndDomain(member.getYear(), domain);
        return CurriculumMapper.INSTANCE.toResponse(curriculums, getExemptions(member.getExemption()), member);
    }

    private Set<SubDomain> getExemptions(Exemption exemption) {
        return exemption != null ? getExemptionCultures(exemption) : new HashSet<>();
    }

    private Set<SubDomain> getExemptionCultures(Exemption exemption) {
        return switch (exemption) {
            case FOREIGN -> Set.of(BASIC_COMPUTER, BASIC_COMPUTER_1, BASIC_COMPUTER_2);
            case DISABLED -> Set.of(BASIC_ENG_MATH);
            default -> new HashSet<>();
        };
    }
}
