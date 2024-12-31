package com.smunity.server.domain.course.controller;

import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.CreditResponseDto;
import com.smunity.server.domain.course.dto.CultureResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.service.CourseCommandService;
import com.smunity.server.domain.course.service.CourseQueryService;
import com.smunity.server.global.common.entity.enums.Category;
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
@RequestMapping("/api/v1/courses")
@Tag(name = "Course API", description = "강의 관련 API")
public class CourseController {

    private final CourseQueryService courseQueryService;
    private final CourseCommandService courseCommandService;

    @GetMapping
    @Operation(
            summary = "강의 조회",
            description = "회원 ID와 카테고리를 기준으로 강의 목록을 조회합니다."
    )
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> readCourses(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        ResultResponseDto<CourseResponseDto> responseDto = courseQueryService.readCourses(memberId, category);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/credit")
    @Operation(
            summary = "강의 학점 조회",
            description = "회원 ID를 기준으로 강의 학점을 조회합니다."
    )
    public ResponseEntity<CreditResponseDto> readCoursesCredit(@AuthMember Long memberId) {
        CreditResponseDto responseDto = courseQueryService.readCoursesCredit(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/cultures/{domain}")
    @Operation(
            summary = "교양 강의 조회",
            description = "회원 ID와 도메인을 기준으로 교양 강의 목록을 조회합니다."
    )
    public ResponseEntity<ResultResponseDto<CultureResponseDto>> readCultureCourses(@AuthMember Long memberId, @PathVariable Domain domain) {
        ResultResponseDto<CultureResponseDto> responseDto = courseQueryService.readCultureCourses(memberId, domain);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/upload")
    @Operation(
            summary = "강의 업로드",
            description = "회원 ID와 요청 데이터를 기준으로 강의를 업로드합니다."
    )
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> uploadCourses(@AuthMember Long memberId, @RequestBody @Valid AuthRequestDto requestDto) {
        ResultResponseDto<CourseResponseDto> responseDto = courseCommandService.createCourses(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
