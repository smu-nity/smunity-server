package com.smunity.server.domain.answer.controller;

import com.smunity.server.domain.answer.dto.AnswerRequestDto;
import com.smunity.server.domain.answer.dto.AnswerResponseDto;
import com.smunity.server.domain.answer.service.AnswerCommandService;
import com.smunity.server.domain.answer.service.AnswerQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions/{questionId}/answer")
public class AnswerController {

    private final AnswerQueryService answerQueryService;
    private final AnswerCommandService answerCommandService;

    @GetMapping
    public ResponseEntity<AnswerResponseDto> getAnswer(@PathVariable Long questionId) {
        AnswerResponseDto answer = answerQueryService.getAnswer(questionId);
        return ResponseEntity.ok(answer);
    }

    @PostMapping
    public ResponseEntity<AnswerResponseDto> createAnswer(@AuthMember Long memberId, @PathVariable Long questionId, @Valid @RequestBody AnswerRequestDto requestDto) {
        AnswerResponseDto responseDto = answerCommandService.createAnswer(memberId, questionId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
