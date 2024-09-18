package com.smunity.server.domain.course.controller;

import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.service.CourseQueryService;
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
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseQueryService courseQueryService;

    @GetMapping
    public ResponseEntity<ResultResponseDto<CourseResponseDto>> getCourses(@AuthMember Long memberId, @RequestParam(required = false) Category category) {
        ResultResponseDto<CourseResponseDto> responseDto = courseQueryService.getCourses(memberId, category);
        return ResponseEntity.ok(responseDto);
    }
}
