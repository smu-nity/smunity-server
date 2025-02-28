package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.dto.LoginRequestDto;
import com.smunity.server.domain.account.entity.LoginStatus;
import com.smunity.server.domain.account.mapper.AccountMapper;
import com.smunity.server.domain.account.repository.LoginStatusRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginStatusService {

    private final MemberRepository memberRepository;
    private final LoginStatusRepository loginStatusRepository;
    private final HttpServletRequest request;

    public void createLoginStatus(LoginRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        LoginStatus loginStatus = AccountMapper.INSTANCE.toEntity(request);
        loginStatus.setMember(member);
        loginStatusRepository.save(loginStatus);
    }
}
