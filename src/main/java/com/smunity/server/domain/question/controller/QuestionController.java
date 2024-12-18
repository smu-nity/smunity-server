package com.smunity.server.domain.question.controller;

import com.smunity.server.domain.question.dto.QuestionReadResponseDto;
import com.smunity.server.domain.question.dto.QuestionRequestDto;
import com.smunity.server.domain.question.dto.QuestionResponseDto;
import com.smunity.server.domain.question.service.QuestionCommandService;
import com.smunity.server.domain.question.service.QuestionQueryService;
import com.smunity.server.global.security.annotation.AuthAdmin;
import com.smunity.server.global.security.annotation.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<QuestionReadResponseDto>> readQuestions(
            @AuthMember Long memberId,
            @ParameterObject @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<QuestionReadResponseDto> responseDtoPage = questionQueryService.readQuestions(memberId, pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionReadResponseDto> readQuestion(@AuthMember Long memberId, @PathVariable Long questionId) {
        QuestionReadResponseDto responseDto = questionQueryService.readQuestion(memberId, questionId);
        return ResponseEntity.ok(responseDto);
    }
    
    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(@AuthMember Long memberId, @RequestBody @Valid QuestionRequestDto requestDto) {
        QuestionResponseDto responseDto = questionCommandService.createQuestion(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(
            @AuthMember Long memberId, @AuthAdmin Boolean isAdmin,
            @PathVariable Long questionId, @RequestBody @Valid QuestionRequestDto requestDto) {
        QuestionResponseDto responseDto = questionCommandService.updateQuestion(memberId, isAdmin, questionId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@AuthMember Long memberId, @AuthAdmin Boolean isAdmin,
                                               @PathVariable Long questionId) {
        questionCommandService.deleteQuestion(memberId, isAdmin, questionId);
        return ResponseEntity.noContent().build();
    }
}
