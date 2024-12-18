package com.smunity.server.domain.member.service;

import com.smunity.server.domain.member.dto.MemberCountDto;
import com.smunity.server.domain.member.dto.MemberInfoResponseDto;
import com.smunity.server.domain.member.dto.MemberResponseDto;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Page<MemberResponseDto> readMembers(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return MemberResponseDto.from(memberPage);
    }

    public MemberInfoResponseDto readMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberInfoResponseDto.from(member);
    }

    public MemberCountDto countMembers() {
        long count = memberRepository.count();
        return MemberCountDto.of(count);
    }
}
