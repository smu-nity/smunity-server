package com.smunity.server.domain.course.controller;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.service.AuthService;
import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.service.CourseCommandService;
import com.smunity.server.domain.course.service.CourseQueryService;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.security.annotation.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseQueryService courseQueryService;
    private final CourseCommandService courseCommandService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> getCourses(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        ResultResponseDto<CourseResponseDto> responseDto = courseQueryService.getCourses(memberId, category);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> uploadCourses(@AuthMember Long memberId, @RequestBody @Valid AuthRequestDto requestDto) {
        List<AuthCourseResponseDto> requestDtoList = authService.getCourses(requestDto);
        ResultResponseDto<CourseResponseDto> responseDto = courseCommandService.createCourses(memberId, requestDtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}