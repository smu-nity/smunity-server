package com.smunity.server.domain.auth.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequest;
import com.smunity.server.domain.auth.dto.AuthResponse;
import com.smunity.server.domain.auth.mapper.AuthMapper;
import com.smunity.server.domain.auth.util.AuthUtil;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public List<AuthCourseResponseDto> readCourses(AuthRequest request) {
        JSONArray response = AuthUtil.getCourses(request);
        return AuthMapper.INSTANCE.toDto(response);
    }

    public AuthResponse authenticate(AuthRequest request) {
        JSONObject response = AuthUtil.getInfo(request);
        String authToken = jwtTokenProvider.createAuthToken(request.username());
        return AuthMapper.INSTANCE.toResponse(response, authToken);
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
