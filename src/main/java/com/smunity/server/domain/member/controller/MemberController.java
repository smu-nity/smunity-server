package com.smunity.server.domain.member.controller;

import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.member.dto.*;
import com.smunity.server.domain.member.service.MemberCommandService;
import com.smunity.server.domain.member.service.MemberQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import com.smunity.server.global.security.annotation.AuthVerified;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<MemberResponseDto>> readMembers(@ParameterObject Pageable pageable) {
        Page<MemberResponseDto> responseDtoPage = memberQueryService.readMembers(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/count")
    public ResponseEntity<MemberCountDto> countMembers() {
        MemberCountDto responseDto = memberQueryService.countMembers();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> readMemberInfo(@AuthMember Long memberId) {
        MemberInfoResponseDto responseDto = memberQueryService.readMember(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> updateMember(@AuthMember Long memberId, @RequestBody @Valid AuthRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.updateMember(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<MemberInfoResponseDto> changePassword(@AuthMember Long memberId, @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changePassword(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/me/department")
    public ResponseEntity<MemberInfoResponseDto> changeDepartment(@AuthMember Long memberId, @RequestBody @Valid ChangeDepartmentRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changeDepartment(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<MemberInfoResponseDto> changePasswordByAuth(@AuthVerified String memberName, @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changePasswordByAuth(memberName, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
