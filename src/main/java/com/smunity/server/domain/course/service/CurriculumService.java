package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.dto.CultureResponseDto;
import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.repository.CurriculumRepository;
import com.smunity.server.global.common.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;

    public List<CultureResponseDto> readCurriculums(Member member, Domain domain) {
        List<Curriculum> curriculums = curriculumRepository.findAllByYearAndDomain(member.getYear(), domain);
        return CultureResponseDto.of(curriculums, member);
    }
}
