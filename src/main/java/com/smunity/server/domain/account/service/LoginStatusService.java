package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.dto.LoginRequest;
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
    private final HttpServletRequest servletRequest;
    private final AccountMapper accountMapper;

    public void createLoginStatus(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        LoginStatus loginStatus = accountMapper.toEntity(servletRequest);
        loginStatus.setMember(member);
        loginStatusRepository.save(loginStatus);
    }
}
