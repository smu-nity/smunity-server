package com.smunity.server.domain.major.service;

import com.smunity.server.domain.major.dto.MajorResponseDto;
import com.smunity.server.domain.major.entity.Major;
import com.smunity.server.domain.major.repository.MajorQueryRepository;
import com.smunity.server.global.common.dto.ListResponseDto;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MajorQueryService {

    private final MemberRepository memberRepository;
    private final MajorQueryRepository majorQueryRepository;

    public ListResponseDto<MajorResponseDto> readMajors(Long memberId, Category category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Major> majors = majorQueryRepository.findByDepartmentAndCategory(member.getDepartment(), category, member.getCompletedNumbers());
        List<MajorResponseDto> responseDtoList = MajorResponseDto.from(majors);
        return ListResponseDto.from(responseDtoList);
    }
}
