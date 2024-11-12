package com.smunity.server.domain.auth.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.domain.auth.util.AuthUtil;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        JSONObject response = AuthUtil.getInfo(requestDto);
        String authToken = jwtTokenProvider.createAuthToken(requestDto.username());
        return AuthResponseDto.of(response, authToken);
    }

    public List<AuthCourseResponseDto> readCourses(AuthRequestDto requestDto) {
        JSONArray response = AuthUtil.getCourses(requestDto);
        return AuthCourseResponseDto.from(response);
    }
}
