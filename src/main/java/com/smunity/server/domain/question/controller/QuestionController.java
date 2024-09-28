package com.smunity.server.domain.question.controller;

import com.smunity.server.domain.question.dto.QuestionRequestDto;
import com.smunity.server.domain.question.dto.QuestionResponseDto;
import com.smunity.server.domain.question.service.QuestionCommandService;
import com.smunity.server.global.security.annotation.AuthMember;
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
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionCommandService questionCommandService;

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(@AuthMember Long memberId, @RequestBody @Valid QuestionRequestDto requestDto) {
        QuestionResponseDto responseDto = questionCommandService.createQuestion(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
