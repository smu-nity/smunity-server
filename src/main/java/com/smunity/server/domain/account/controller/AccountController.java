package com.smunity.server.domain.account.controller;

import com.smunity.server.domain.account.dto.*;
import com.smunity.server.domain.account.service.AccountService;
import com.smunity.server.global.security.annotation.AuthVerified;
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
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@AuthVerified String memberName, @RequestBody @Valid RegisterRequestDto requestDto) {
        RegisterResponseDto responseDto = accountService.register(memberName, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto responseDto = accountService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody @Valid RefreshRequestDto requestDto) {
        LoginResponseDto responseDto = accountService.refresh(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
