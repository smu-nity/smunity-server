package com.smunity.server.domain.account.service;

import com.smunity.server.domain.account.dto.*;
import com.smunity.server.domain.account.entity.RefreshToken;
import com.smunity.server.domain.account.mapper.AccountMapper;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.common.repository.DepartmentRepository;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.common.repository.YearRepository;
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
    private final YearRepository yearRepository;
    private final DepartmentRepository departmentRepository;
    private final LoginStatusService loginStatusService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterResponseDto register(String memberName, RegisterRequestDto requestDto) {
        validateUser(memberName, requestDto.username());
        Member member = AccountMapper.INSTANCE.toEntity(requestDto);
        Year year = yearRepository.findByName(requestDto.username().substring(0, 4))
                .orElseThrow(() -> new GeneralException(ErrorCode.YEAR_NOT_FOUND));
        Department department = departmentRepository.findByName(requestDto.department())
                .orElseThrow(() -> new GeneralException(ErrorCode.DEPARTMENT_NOT_FOUND));
        String encodedPw = passwordEncoder.encode(requestDto.password());
        member.setInfo(year, department, encodedPw);
        return AccountMapper.INSTANCE.toDto(memberRepository.save(member));
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.username())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        checkPassword(requestDto.password(), member.getPassword());
        loginStatusService.createLoginStatus(requestDto);
        return generateToken(member.getUsername(), member.getId(), member.getRole());
    }

    public LoginResponseDto refresh(RefreshRequestDto requestDto) {
        jwtTokenProvider.validateToken(requestDto.refreshToken(), true);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(requestDto.refreshToken());
        Member member = memberRepository.findById(oldRefreshToken.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
        return generateToken(member.getUsername(), member.getId(), member.getRole());
    }

    public void logout(Long memberId, RefreshRequestDto requestDto) {
        jwtTokenProvider.validateToken(requestDto.refreshToken(), true);
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(requestDto.refreshToken());
        validateMember(memberId, oldRefreshToken);
        refreshTokenService.deleteRefreshToken(oldRefreshToken.getToken());
    }

    private LoginResponseDto generateToken(String username, Long memberId, MemberRole memberRole) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId, memberRole, false);
        String refreshToken = jwtTokenProvider.createAccessToken(memberId, memberRole, true);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);
        return new LoginResponseDto(username, memberRole, accessToken, refreshToken);
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
