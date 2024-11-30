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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseQueryService courseQueryService;
    private final CourseCommandService courseCommandService;

    @GetMapping
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> readCourses(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        ResultResponseDto<CourseResponseDto> responseDto = courseQueryService.readCourses(memberId, category);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/credit")
    public ResponseEntity<CreditResponseDto> readCoursesCredit(@AuthMember Long memberId) {
        CreditResponseDto responseDto = courseQueryService.readCoursesCredit(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/cultures/{domain}")
    public ResponseEntity<ResultResponseDto<CultureResponseDto>> readCultureCourses(@AuthMember Long memberId, @PathVariable Domain domain) {
        ResultResponseDto<CultureResponseDto> responseDto = courseQueryService.readCultureCourses(memberId, domain);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> uploadCourses(@AuthMember Long memberId, @RequestBody @Valid AuthRequestDto requestDto) {
        ResultResponseDto<CourseResponseDto> responseDto = courseCommandService.createCourses(memberId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
