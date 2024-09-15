package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.dto.RegisterRequestDto;
import com.smunity.server.domain.account.dto.RegisterResponseDto;
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
public class AccountService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        validateUsername(requestDto.username());
        String encodedPw = passwordEncoder.encode(requestDto.password());
        Member member = requestDto.toEntity(encodedPw);
        return RegisterResponseDto.from(memberRepository.save(member));
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new GeneralException(ErrorCode.ACCOUNT_CONFLICT);
        }
    }
}
