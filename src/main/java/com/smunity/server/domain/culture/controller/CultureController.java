package com.smunity.server.domain.culture.controller;

import com.smunity.server.domain.culture.dto.CultureResponseDto;
import com.smunity.server.domain.culture.service.CultureQueryService;
import com.smunity.server.domain.member.dto.SubjectResponseDto;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cultures")
public class CultureController {

    private final CultureQueryService cultureQueryService;

    @GetMapping
    public ResponseEntity<SubjectResponseDto<CultureResponseDto>> readCultures(@RequestParam(required = false) SubDomain subDomain) {
        SubjectResponseDto<CultureResponseDto> responseDto = cultureQueryService.readCultures(subDomain);
        return ResponseEntity.ok(responseDto);
    }
}
