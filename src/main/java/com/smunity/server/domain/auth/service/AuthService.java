package com.smunity.server.domain.auth.service;

import com.smunity.AuthManager;
import com.smunity.dto.AuthResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequest;
import com.smunity.server.domain.auth.dto.AuthResponse;
import com.smunity.server.domain.auth.mapper.AuthMapper;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse authenticate(AuthRequest request) {
        AuthResponseDto responseDto = AuthManager.authenticate(request.username(), request.password());
        String authToken = jwtTokenProvider.createAuthToken(request.username());
        return AuthMapper.INSTANCE.toResponse(responseDto, authToken);
    }

    public AuthResponse registerAuth(AuthRequest request) {
        validateUsername(request.username());
        return authenticate(request);
    }

    public AuthResponse resetPassword(AuthRequest request) {
        validateExistingUsername(request.username());
        return authenticate(request);
    }

    private void validateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new GeneralException(ErrorCode.ACCOUNT_CONFLICT);
        }
    }

    private void validateExistingUsername(String username) {
        if (!memberRepository.existsByUsername(username)) {
            throw new GeneralException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
