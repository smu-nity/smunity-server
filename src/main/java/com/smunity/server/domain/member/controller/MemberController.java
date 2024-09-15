package com.smunity.server.domain.member.controller;

import com.smunity.server.domain.member.dto.MemberInfoResponseDto;
import com.smunity.server.domain.member.service.MemberCommandService;
import com.smunity.server.domain.member.service.MemberQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import com.smunity.server.global.validation.annotation.PermissionCheck;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    public ResponseEntity<Page<MemberInfoResponseDto>> findAll(@ParameterObject Pageable pageable) {
        Page<MemberInfoResponseDto> responseDtoPage = memberQueryService.findAll(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoResponseDto> findById(@PermissionCheck @PathVariable Long id) {
        MemberInfoResponseDto responseDto = memberQueryService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo(@AuthMember Long memberId) {
        MemberInfoResponseDto responseDto = memberQueryService.findById(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
