package com.smunity.server.domain.answer.controller;

import com.smunity.server.domain.answer.dto.AnswerRequestDto;
import com.smunity.server.domain.answer.dto.AnswerResponseDto;
import com.smunity.server.domain.answer.service.AnswerCommandService;
import com.smunity.server.domain.answer.service.AnswerQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions/{questionId}/answer")
@Tag(name = "09 Answer API", description = "답변 관련 API")
public class AnswerController {

    private final AnswerQueryService answerQueryService;
    private final AnswerCommandService answerCommandService;

    @GetMapping
    @Operation(summary = "답변 조회", description = "질문에 대한 답변을 조회합니다.")
    public ResponseEntity<AnswerResponseDto> readAnswer(@PathVariable Long questionId) {
        AnswerResponseDto responseDto = answerQueryService.readAnswer(questionId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    @Operation(summary = "답변 생성", description = "관리자가 질문에 대한 답변을 생성합니다.")
    public ResponseEntity<AnswerResponseDto> createAnswer(@AuthMember Long memberId, @PathVariable Long questionId,
                                                          @RequestBody @Valid AnswerRequestDto requestDto) {
        AnswerResponseDto responseDto = answerCommandService.createAnswer(memberId, questionId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping
    @Operation(summary = "답변 수정", description = "관리자가 질문에 대한 답변을 수정합니다.")
    public ResponseEntity<AnswerResponseDto> updateAnswer(@AuthMember Long memberId, @PathVariable Long questionId,
                                                          @RequestBody @Valid AnswerRequestDto requestDto) {
        AnswerResponseDto responseDto = answerCommandService.updateAnswer(memberId, questionId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping
    @Operation(summary = "답변 삭제", description = " 관리자가 질문에 대한 답변을 삭제합니다.")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long questionId) {
        answerCommandService.deleteAnswer(questionId);
        return ResponseEntity.noContent().build();
    }
}
