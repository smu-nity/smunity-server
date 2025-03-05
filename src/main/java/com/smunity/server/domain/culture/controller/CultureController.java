package com.smunity.server.domain.culture.controller;

import com.smunity.server.domain.culture.dto.CultureResponse;
import com.smunity.server.domain.culture.service.CultureQueryService;
import com.smunity.server.global.common.dto.ListResponse;
import com.smunity.server.global.common.entity.enums.SubDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cultures")
@Tag(name = "07 Culture API", description = "교양과목 관련 API")
public class CultureController {

    private final CultureQueryService cultureQueryService;

    @GetMapping
    @Operation(summary = "교양과목 목록 조회", description = "교양 영역을 기준으로 교양과목 목록을 조회합니다.")
    public ResponseEntity<ListResponse<CultureResponse>> readCultures(@RequestParam(required = false) SubDomain subDomain) {
        ListResponse<CultureResponse> response = cultureQueryService.readCultures(subDomain);
        return ResponseEntity.ok(response);
    }
}
