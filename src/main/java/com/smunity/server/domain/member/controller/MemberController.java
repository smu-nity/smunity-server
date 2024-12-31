package com.smunity.server.domain.member.controller;

import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.member.dto.*;
import com.smunity.server.domain.member.service.MemberCommandService;
import com.smunity.server.domain.member.service.MemberQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import com.smunity.server.global.security.annotation.AuthVerified;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "03 Member API", description = "회원 관련 API")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    @Operation(summary = "회원 목록 조회", description = "회원 목록을 페이징 처리하여 조회합니다.")
    public ResponseEntity<Page<MemberResponseDto>> readMembers(@ParameterObject Pageable pageable) {
        Page<MemberResponseDto> responseDtoPage = memberQueryService.readMembers(pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    @GetMapping("/count")
    @Operation(summary = "회원 수 조회", description = "전체 회원 수를 조회합니다.")
    public ResponseEntity<MemberCountDto> countMembers() {
        MemberCountDto responseDto = memberQueryService.countMembers();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인한 회원의 정보를 조회합니다.")
    public ResponseEntity<MemberInfoResponseDto> readMemberInfo(@AuthMember Long memberId) {
        MemberInfoResponseDto responseDto = memberQueryService.readMember(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/me")
    @Operation(summary = "내 정보 수정", description = "로그인한 회원의 정보를 수정합니다.")
    public ResponseEntity<MemberInfoResponseDto> updateMember(@AuthMember Long memberId, @RequestBody @Valid AuthRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.updateMember(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/me/password")
    @Operation(summary = "비밀번호 변경", description = "로그인한 회원의 비밀번호를 변경합니다.")
    public ResponseEntity<MemberInfoResponseDto> changePassword(@AuthMember Long memberId, @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changePassword(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/me/department")
    @Operation(summary = "학과 변경", description = "로그인한 회원의 학과를 변경합니다.")
    public ResponseEntity<MemberInfoResponseDto> changeDepartment(@AuthMember Long memberId, @RequestBody @Valid ChangeDepartmentRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changeDepartment(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정", description = "인증 토큰 검증 후 비밀번호를 변경합니다.")
    public ResponseEntity<MemberInfoResponseDto> changePasswordByAuth(@AuthVerified String memberName, @RequestBody @Valid ChangePasswordRequestDto requestDto) {
        MemberInfoResponseDto responseDto = memberCommandService.changePasswordByAuth(memberName, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    @Operation(summary = "내 계정 삭제", description = "로그인한 회원의 계정을 삭제합니다.")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
