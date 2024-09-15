package com.smunity.server.domain.account.controller;

import com.smunity.server.domain.account.dto.RegisterRequestDto;
import com.smunity.server.domain.account.dto.RegisterResponseDto;
import com.smunity.server.domain.account.service.AccountService;
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
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        RegisterResponseDto responseDto = accountService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
