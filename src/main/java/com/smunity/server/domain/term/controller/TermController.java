package com.smunity.server.domain.term.controller;

import com.smunity.server.domain.term.dto.TermResponseDto;
import com.smunity.server.domain.term.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terms")
public class TermController {

    private final TermService termService;

    @GetMapping("/current")
    public ResponseEntity<TermResponseDto> readCurrentTerm() {
        TermResponseDto responseDto = termService.readCurrentTerm();
        return ResponseEntity.ok(responseDto);
    }
}
