package com.smunity.server.domain.department.controller;

import com.smunity.server.domain.department.dto.DepartmentEditResponse;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.service.DepartmentService;
import com.smunity.server.global.common.dto.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
@Tag(name = "04 Department API", description = "학과 관련 API")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "학과 조회", description = "전체 학과 목록을 조회합니다.")
    public ResponseEntity<ListResponse<DepartmentResponse>> readDepartments() {
        ListResponse<DepartmentResponse> response = departmentService.readDepartments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/editable")
    @Operation(summary = "수정 가능한 학과 조회", description = "수정 가능한 학과 목록을 조회합니다.")
    public ResponseEntity<ListResponse<DepartmentEditResponse>> readEditableDepartments() {
        ListResponse<DepartmentEditResponse> response = departmentService.readEditableDepartments();
        return ResponseEntity.ok(response);
    }
}
