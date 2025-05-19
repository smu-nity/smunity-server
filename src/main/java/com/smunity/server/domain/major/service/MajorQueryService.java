package com.smunity.server.domain.major.service;

import com.smunity.server.domain.major.dto.MajorResponse;
import com.smunity.server.domain.major.entity.Major;
import com.smunity.server.domain.major.mapper.MajorMapper;
import com.smunity.server.domain.major.repository.MajorQueryRepository;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.smunity.server.global.common.entity.enums.Category.getMajorCategory;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MajorQueryService {

    private final MemberRepository memberRepository;
    private final MajorQueryRepository majorQueryRepository;

    public ListResponse<MajorResponse> readMajors(Long memberId, Category category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Major> majors = majorQueryRepository.findByDepartmentAndCategory(member.getDepartment(category), getMajorCategory(category), member.getCompletedNumbers());
        return MajorMapper.INSTANCE.toResponse(majors);
    }
}
