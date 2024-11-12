package com.smunity.server.domain.auth.controller;

import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.domain.auth.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.authenticate(requestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerAuth(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.registerAuth(requestDto));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<AuthResponseDto> resetPassword(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(authService.resetPassword(requestDto));
    }
}
