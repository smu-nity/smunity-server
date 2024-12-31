package com.smunity.server.domain.major.controller;

import com.smunity.server.domain.major.dto.MajorResponseDto;
import com.smunity.server.domain.major.service.MajorQueryService;
import com.smunity.server.global.common.dto.ListResponseDto;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.security.annotation.AuthMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/majors")
@Tag(name = "06 Major API", description = "전공과목 관련 API")
public class MajorController {

    private final MajorQueryService majorQueryService;

    @GetMapping
    @Operation(summary = "전공과목 목록 조회", description = "이수구분을 기준으로 로그인한 회원이 이수하지 않은 전공과목 목록을 조회합니다.")
    public ResponseEntity<ListResponseDto<MajorResponseDto>> readMajors(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        ListResponseDto<MajorResponseDto> responseDto = majorQueryService.readMajors(memberId, category);
        return ResponseEntity.ok(responseDto);
    }
}
