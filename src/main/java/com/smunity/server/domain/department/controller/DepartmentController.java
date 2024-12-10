package com.smunity.server.domain.department.controller;

import com.smunity.server.domain.department.dto.DepartmentResponseDto;
import com.smunity.server.domain.department.service.DepartmentService;
import com.smunity.server.global.common.dto.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ListResponseDto<DepartmentResponseDto>> readDepartments(@RequestParam(required = false) Boolean isEditable) {
        ListResponseDto<DepartmentResponseDto> responseDto = departmentService.readDepartments(isEditable);
        return ResponseEntity.ok(responseDto);
    }
}
