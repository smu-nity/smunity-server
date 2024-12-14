package com.smunity.server.domain.department.controller;

import com.smunity.server.domain.department.dto.DepartmentEditResponseDto;
import com.smunity.server.domain.department.service.DepartmentService;
import com.smunity.server.global.common.dto.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/editable")
    public ResponseEntity<ListResponseDto<DepartmentEditResponseDto>> readEditableDepartments() {
        ListResponseDto<DepartmentEditResponseDto> responseDto = departmentService.readEditableDepartments();
        return ResponseEntity.ok(responseDto);
    }
}
