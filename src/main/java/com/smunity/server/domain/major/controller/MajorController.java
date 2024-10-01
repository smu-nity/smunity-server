package com.smunity.server.domain.major.controller;

import com.smunity.server.domain.major.dto.MajorResponseDto;
import com.smunity.server.domain.major.service.MajorQueryService;
import com.smunity.server.domain.member.dto.SubjectResponseDto;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.security.annotation.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/majors")
public class MajorController {

    private final MajorQueryService majorQueryService;

    @GetMapping
    public ResponseEntity<SubjectResponseDto<MajorResponseDto>> readMajors(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        SubjectResponseDto<MajorResponseDto> responseDto = majorQueryService.readMajors(memberId, category);
        return ResponseEntity.ok(responseDto);
    }
}
