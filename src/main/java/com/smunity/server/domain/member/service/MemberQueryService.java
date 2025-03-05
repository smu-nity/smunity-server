package com.smunity.server.domain.member.service;

import com.smunity.server.domain.member.dto.MemberCountResponse;
import com.smunity.server.domain.member.dto.MemberInfoResponse;
import com.smunity.server.domain.member.dto.MemberResponse;
import com.smunity.server.domain.member.mapper.MemberMapper;
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

    public Page<MemberResponse> readMembers(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return MemberMapper.INSTANCE.toResponse(memberPage);
    }

    public MemberInfoResponse readMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberMapper.INSTANCE.toResponse(member);
    }

    public MemberCountResponse countMembers() {
        long count = memberRepository.count();
        return MemberMapper.INSTANCE.toResponse(count);
    }
}
