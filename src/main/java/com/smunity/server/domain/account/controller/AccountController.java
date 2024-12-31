package com.smunity.server.domain.account.controller;

import com.smunity.server.domain.account.dto.*;
import com.smunity.server.domain.account.service.AccountService;
import com.smunity.server.global.security.annotation.AuthVerified;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account API", description = "계정 관련 API")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원 정보를 바탕으로 회원을 등록합니다.")
    public ResponseEntity<RegisterResponseDto> register(@AuthVerified String memberName, @RequestBody @Valid RegisterRequestDto requestDto) {
        RegisterResponseDto responseDto = accountService.register(memberName, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자의 로그인 정보를 검증하고 액세스 토큰과 리프레시 토큰을 반환합니다.")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto responseDto = accountService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 통해 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody @Valid RefreshRequestDto requestDto) {
        LoginResponseDto responseDto = accountService.refresh(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
