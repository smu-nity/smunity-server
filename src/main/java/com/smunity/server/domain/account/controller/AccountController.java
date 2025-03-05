package com.smunity.server.domain.account.controller;

import com.smunity.server.domain.account.dto.*;
import com.smunity.server.domain.account.service.AccountService;
import com.smunity.server.global.security.annotation.AuthMember;
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
@Tag(name = "01 Account API", description = "계정 관련 API")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원 정보를 바탕으로 회원을 등록합니다.")
    public ResponseEntity<RegisterResponse> register(@AuthVerified String memberName, @RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = accountService.register(memberName, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자의 로그인 정보를 검증하고 액세스 토큰과 리프레시 토큰을 반환합니다.")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = accountService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 통해 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    public ResponseEntity<LoginResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        LoginResponse response = accountService.refresh(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "동일 사용자 검증 후 리프레시 토큰을 무효화합니다.")
    public ResponseEntity<Void> logout(@AuthMember Long memberId, @RequestBody @Valid RefreshRequest request) {
        accountService.logout(memberId, request);
        return ResponseEntity.noContent().build();
    }
}
