package com.smunity.server.domain.question.controller;

import com.smunity.server.domain.question.dto.QuestionReadResponse;
import com.smunity.server.domain.question.dto.QuestionRequest;
import com.smunity.server.domain.question.dto.QuestionResponse;
import com.smunity.server.domain.question.service.QuestionCommandService;
import com.smunity.server.domain.question.service.QuestionQueryService;
import com.smunity.server.global.security.annotation.AuthAdmin;
import com.smunity.server.global.security.annotation.AuthMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "08 Question API", description = "질문 관련 API")
public class QuestionController {

    private final QuestionQueryService questionQueryService;
    private final QuestionCommandService questionCommandService;

    @GetMapping
    @Operation(summary = "질문 목록 조회", description = "질문 목록을 페이징 처리하여 조회합니다.")
    public ResponseEntity<Page<QuestionReadResponse>> readQuestions(
            @AuthMember Long memberId,
            @ParameterObject @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<QuestionReadResponse> responses = questionQueryService.readQuestions(memberId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "질문 조회", description = "질문을 조회합니다.")
    public ResponseEntity<QuestionReadResponse> readQuestion(@AuthMember Long memberId, @PathVariable Long questionId) {
        QuestionReadResponse response = questionQueryService.readQuestion(memberId, questionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "질문 생성", description = "로그인한 회원으로 질문을 생성합니다.")
    public ResponseEntity<QuestionResponse> createQuestion(@AuthMember Long memberId, @RequestBody @Valid QuestionRequest request) {
        QuestionResponse response = questionCommandService.createQuestion(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "질문 수정", description = "로그인한 회원이 본인이 작성한 질문을 수정합니다.")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @AuthMember Long memberId, @AuthAdmin Boolean isAdmin,
            @PathVariable Long questionId, @RequestBody @Valid QuestionRequest request) {
        QuestionResponse response = questionCommandService.updateQuestion(memberId, isAdmin, questionId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{questionId}")
    @Operation(summary = "질문 삭제", description = "로그인한 회원이 본인이 작성한 질문을 삭제합니다.")
    public ResponseEntity<Void> deleteQuestion(@AuthMember Long memberId, @AuthAdmin Boolean isAdmin,
                                               @PathVariable Long questionId) {
        questionCommandService.deleteQuestion(memberId, isAdmin, questionId);
        return ResponseEntity.noContent().build();
    }
}
