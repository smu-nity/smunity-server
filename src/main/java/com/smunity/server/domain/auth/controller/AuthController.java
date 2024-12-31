package com.smunity.server.domain.auth.controller;

import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "02 Auth API", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    @Operation(summary = "사용자 인증", description = "사용자 인증 요청을 검증하고 인증 토큰을 반환합니다.")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.authenticate(requestDto));
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 사용자 인증", description = "회원가입을 위한 사용자 인증 토큰을 반환합니다.")
    public ResponseEntity<AuthResponseDto> registerAuth(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.registerAuth(requestDto));
    }

    @PostMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정 사용자 인증", description = "사용자의 비밀번호 재설정을 위한 사용자 인증 토큰을 반환합니다.")
    public ResponseEntity<AuthResponseDto> resetPassword(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.resetPassword(requestDto));
    }
}
