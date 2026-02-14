package com.smunity.server.domain.member.controller;

import com.smunity.server.domain.auth.dto.AuthRequest;
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
    public ResponseEntity<Page<MemberResponse>> readMembers(@ParameterObject Pageable pageable) {
        Page<MemberResponse> responses = memberQueryService.readMembers(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/count")
    @Operation(summary = "회원 수 조회", description = "전체 회원 수를 조회합니다.")
    public ResponseEntity<MemberCountResponse> countMembers() {
        MemberCountResponse response = memberQueryService.countMembers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인한 회원의 정보를 조회합니다.")
    public ResponseEntity<MemberInfoResponse> readMemberInfo(@AuthMember Long memberId) {
        MemberInfoResponse response = memberQueryService.readMember(memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @Operation(summary = "내 정보 수정", description = "로그인한 회원의 정보를 수정합니다.")
    public ResponseEntity<MemberInfoResponse> updateMember(@AuthMember Long memberId,
                                                           @RequestBody @Valid AuthRequest request) {
        MemberInfoResponse response = memberCommandService.updateMember(memberId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/password")
    @Operation(summary = "비밀번호 변경", description = "로그인한 회원의 비밀번호를 변경합니다.")
    public ResponseEntity<MemberInfoResponse> changePassword(@AuthMember Long memberId,
                                                             @RequestBody @Valid ChangePasswordRequest request) {
        MemberInfoResponse response = memberCommandService.changePassword(memberId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/department")
    @Operation(summary = "학과 변경", description = "로그인한 회원의 학과를 변경합니다.")
    public ResponseEntity<MemberInfoResponse> changeDepartment(@AuthMember Long memberId,
                                                               @RequestBody @Valid ChangeDepartmentRequest request) {
        MemberInfoResponse response = memberCommandService.changeDepartment(memberId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/exemption")
    @Operation(summary = "이수 면제 설정 변경", description = "로그인한 회원의 이수 면제 설정을 변경합니다.")
    public ResponseEntity<MemberInfoResponse> changeExemption(@AuthMember Long memberId,
                                                              @RequestBody @Valid ChangeExemptionRequest request) {
        MemberInfoResponse response = memberCommandService.changeExemption(memberId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정", description = "인증 토큰 검증 후 비밀번호를 변경합니다.")
    public ResponseEntity<MemberInfoResponse> changePasswordByAuth(@AuthVerified String username,
                                                                   @RequestBody @Valid ChangePasswordRequest request) {
        MemberInfoResponse response = memberCommandService.changePasswordByAuth(username, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    @Operation(summary = "내 계정 삭제", description = "로그인한 회원의 계정을 삭제합니다.")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
