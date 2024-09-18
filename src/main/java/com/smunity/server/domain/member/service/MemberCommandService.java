package com.smunity.server.domain.member.service;

import com.smunity.server.domain.member.dto.ChangePasswordRequestDto;
import com.smunity.server.domain.member.dto.MemberInfoResponseDto;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public MemberInfoResponseDto changePassword(Long memberId, ChangePasswordRequestDto requestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        String newEncodedPw = passwordEncoder.encode(requestDto.password());
        member.changePassword(newEncodedPw);
        return MemberInfoResponseDto.from(member);
    }
}
