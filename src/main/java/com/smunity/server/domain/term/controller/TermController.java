package com.smunity.server.domain.term.controller;

import com.smunity.server.domain.term.dto.TermResponse;
import com.smunity.server.domain.term.service.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/terms")
@Tag(name = "10 Term API", description = "학기 관련 API")
public class TermController {

    private final TermService termService;

    @GetMapping("/current")
    @Operation(summary = "현재 학기 조회", description = "현재 학기를 조회합니다.")
    public ResponseEntity<TermResponse> readCurrentTerm() {
        TermResponse response = termService.readCurrentTerm();
        return ResponseEntity.ok(response);
    }
}
