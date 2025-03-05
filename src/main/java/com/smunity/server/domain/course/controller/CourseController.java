package com.smunity.server.domain.course.controller;

import com.smunity.server.domain.auth.dto.AuthRequest;
import com.smunity.server.domain.course.dto.CourseResponse;
import com.smunity.server.domain.course.dto.CreditResponse;
import com.smunity.server.domain.course.dto.CultureResponse;
import com.smunity.server.domain.course.dto.ResultResponse;
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
@Tag(name = "05 Course API", description = "이수과목 관련 API")
public class CourseController {

    private final CourseQueryService courseQueryService;
    private final CourseCommandService courseCommandService;

    @GetMapping
    @Operation(summary = "이수과목 조회", description = "이수구분을 기준으로 로그인한 회원의 이수과목 목록을 조회합니다.")
    public ResponseEntity<ResultResponse<CourseResponse>> readCourses(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        ResultResponse<CourseResponse> response = courseQueryService.readCourses(memberId, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/credit")
    @Operation(summary = "이수과목 학점 조회", description = "로그인한 회원의 이수과목 학점을 조회합니다.")
    public ResponseEntity<CreditResponse> readCoursesCredit(@AuthMember Long memberId) {
        CreditResponse response = courseQueryService.readCoursesCredit(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cultures/{domain}")
    @Operation(summary = "이수 교양과목 강의 조회", description = "교양 이수구분을 기준으로 로그인한 회원의 이수 교양과목 목록을 조회합니다.")
    public ResponseEntity<ResultResponse<CultureResponse>> readCultureCourses(@AuthMember Long memberId, @PathVariable Domain domain) {
        ResultResponse<CultureResponse> response = courseQueryService.readCultureCourses(memberId, domain);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    @Operation(summary = "이수과목 업로드", description = "로그인한 회원의 이수과목을 업로드합니다.")
    public ResponseEntity<ResultResponse<CourseResponse>> uploadCourses(@AuthMember Long memberId, @RequestBody @Valid AuthRequest request) {
        ResultResponse<CourseResponse> response = courseCommandService.createCourses(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
