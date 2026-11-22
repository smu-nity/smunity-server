package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.dto.*;
import com.smunity.server.domain.account.entity.RefreshToken;
import com.smunity.server.domain.account.mapper.AccountMapper;
import com.smunity.server.domain.department.service.DepartmentService;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.common.repository.YearRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import com.smunity.server.global.security.provider.TokenType;
import com.smunity.server.global.security.provider.VerifiedInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final YearRepository yearRepository;
    private final DepartmentService departmentService;
    private final LoginStatusService loginStatusService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountMapper accountMapper;

    public RegisterResponse register(VerifiedInfo info, RegisterRequest request) {
        validateUsername(info.username());
        Member member = accountMapper.toEntity(info);
        Year year = findYearByUsername(info.username());
        Department department = departmentService.findDepartmentByName(info.department());
        Department secondDepartment = departmentService.findDepartmentByName(info.secondDepartment());
        String encodedPw = passwordEncoder.encode(request.password());
        member.setInfo(year, department, secondDepartment, info.teachingTrack(), encodedPw);
        return accountMapper.toResponse(memberRepository.save(member));
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        checkPassword(request.password(), member.getPassword());
        loginStatusService.createLoginStatus(request);
        return generateToken(member.getUsername(), member.getId(), member.getRole());
    }

    public LoginResponse refresh(RefreshRequest request) {
        jwtTokenProvider.validateToken(request.refreshToken(), TokenType.REFRESH);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(request.refreshToken());
        Member member = memberRepository.findById(oldRefreshToken.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
        return generateToken(member.getUsername(), member.getId(), member.getRole());
    }

    public void logout(Long memberId, RefreshRequest request) {
        jwtTokenProvider.validateToken(request.refreshToken(), TokenType.REFRESH);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(request.refreshToken());
        validateMember(memberId, oldRefreshToken);
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
    }

    private Year findYearByUsername(String username) {
        int year = Integer.parseInt(username.substring(0, 4));
        return (year >= 2017 ? yearRepository.findByValue(year) : yearRepository.findById(1L)).orElseThrow(() -> new GeneralException(ErrorCode.YEAR_NOT_FOUND));
    }

    private LoginResponse generateToken(String username, Long memberId, MemberRole memberRole) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, memberRole);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId, memberRole);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return accountMapper.toResponse(username, memberRole, accessToken, refreshToken);
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new GeneralException(ErrorCode.ACCOUNT_CONFLICT);
        }
    }

    private void validateMember(Long memberId, RefreshToken refreshToken) {
        if (!memberId.equals(refreshToken.getMemberId())) {
            throw new GeneralException(ErrorCode.FORBIDDEN_EXCEPTION);
        }
    }

    private void checkPassword(String requestPassword, String memberPassword) {
        if (!passwordEncoder.matches(requestPassword, memberPassword)) {
            throw new GeneralException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
