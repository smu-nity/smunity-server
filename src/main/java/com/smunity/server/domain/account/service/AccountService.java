package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.dto.*;
import com.smunity.server.domain.account.entity.RefreshToken;
import com.smunity.server.domain.account.mapper.AccountMapper;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.common.exception.DepartmentNotFoundException;
import com.smunity.server.global.common.repository.DepartmentRepository;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.common.service.YearService;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final YearService yearService;
    private final LoginStatusService loginStatusService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterResponse register(String memberName, RegisterRequest request) {
        validateUser(memberName, request.username());
        Member member = AccountMapper.INSTANCE.toEntity(request);
        Year year = yearService.findByUsername(request.username());
        Department department = departmentRepository.findByName(request.department())
                .orElseThrow(() -> new DepartmentNotFoundException(request.department()));
        String encodedPw = passwordEncoder.encode(request.password());
        member.setInfo(year, department, encodedPw);
        return AccountMapper.INSTANCE.toResponse(memberRepository.save(member));
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        checkPassword(request.password(), member.getPassword());
        loginStatusService.createLoginStatus(request);
        return generateToken(member.getUsername(), member.getId(), member.getRole());
    }

    public LoginResponse refresh(RefreshRequest request) {
        jwtTokenProvider.validateToken(request.refreshToken(), true);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(request.refreshToken());
        Member member = memberRepository.findById(oldRefreshToken.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
        return generateToken(member.getUsername(), member.getId(), member.getRole());
    }

    public void logout(Long memberId, RefreshRequest request) {
        jwtTokenProvider.validateToken(request.refreshToken(), true);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(request.refreshToken());
        validateMember(memberId, oldRefreshToken);
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
    }

    private LoginResponse generateToken(String username, Long memberId, MemberRole memberRole) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, memberRole, false);
        String refreshToken = jwtTokenProvider.createAccessToken(memberId, memberRole, true);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return AccountMapper.INSTANCE.toResponse(username, memberRole, accessToken, refreshToken);
    }

    private void validateUser(String memberName, String username) {
        validateVerified(memberName, username);
        validateUsername(username);
    }

    private void validateVerified(String verifiedUser, String username) {
        if (!verifiedUser.equals(username)) {
            throw new GeneralException(ErrorCode.UNVERIFIED_USER);
        }
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
