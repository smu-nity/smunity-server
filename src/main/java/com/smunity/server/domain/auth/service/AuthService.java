package com.smunity.server.domain.auth.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
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

    public List<AuthCourseResponseDto> readCourses(AuthRequestDto requestDto) {
        JSONArray response = AuthUtil.getCourses(requestDto);
        return AuthMapper.INSTANCE.toDto(response);
    }

    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        JSONObject response = AuthUtil.getInfo(requestDto);
        String authToken = jwtTokenProvider.createAuthToken(requestDto.username());
        return AuthMapper.INSTANCE.toDto(response, authToken);
    }

    public AuthResponseDto registerAuth(AuthRequestDto requestDto) {
        validateUsername(requestDto.username());
        return authenticate(requestDto);
    }

    public AuthResponseDto resetPassword(AuthRequestDto requestDto) {
        validateExistingUsername(requestDto.username());
        return authenticate(requestDto);
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
