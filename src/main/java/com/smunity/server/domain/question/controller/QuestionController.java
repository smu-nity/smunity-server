package com.smunity.server.domain.question.controller;

import com.smunity.server.domain.question.dto.QuestionRequestDto;
import com.smunity.server.domain.question.dto.QuestionResponseDto;
import com.smunity.server.domain.question.service.QuestionCommandService;
import com.smunity.server.domain.question.service.QuestionQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionQueryService questionQueryService;
    private final QuestionCommandService questionCommandService;

    @GetMapping
    public ResponseEntity<Page<QuestionResponseDto>> getQuestions(@ParameterObject Pageable pageable) {
        Page<QuestionResponseDto> responseDtoPage = questionQueryService.getQuestions(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(@AuthMember Long memberId, @RequestBody @Valid QuestionRequestDto requestDto) {
        QuestionResponseDto responseDto = questionCommandService.createQuestion(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
