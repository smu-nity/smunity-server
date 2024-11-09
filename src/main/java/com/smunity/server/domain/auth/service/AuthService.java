package com.smunity.server.domain.auth.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.domain.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        JSONArray response = AuthUtil.getData(requestDto, "/UsrSchMng/selectStdInfo.do", "dsStdInfoList");
        return AuthResponseDto.from(response);
    }

    public List<AuthCourseResponseDto> readCourses(AuthRequestDto requestDto) {
        JSONArray response = AuthUtil.getData(requestDto, "/UsrRecMatt/list.do", "dsRecMattList");
        return AuthCourseResponseDto.from(response);
    }
}
