package com.smunity.server.domain.member.controller;

import com.smunity.server.domain.member.dto.ChangePasswordRequestDto;
import com.smunity.server.domain.member.dto.MemberResponseDto;
import com.smunity.server.domain.member.service.MemberCommandService;
import com.smunity.server.domain.member.service.MemberQueryService;
import com.smunity.server.global.security.annotation.AuthMember;
import com.smunity.server.global.validation.annotation.PermissionCheck;
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

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> readMember(@PermissionCheck @PathVariable Long id) {
        MemberResponseDto responseDto = memberQueryService.readMember(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> readMemberInfo(@AuthMember Long memberId) {
        MemberResponseDto responseDto = memberQueryService.readMember(memberId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@AuthMember Long memberId) {
        memberCommandService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<MemberResponseDto> changePassword(@AuthMember Long memberId,
                                                            @Valid @RequestBody ChangePasswordRequestDto requestDto) {
        MemberResponseDto responseDto = memberCommandService.changePassword(memberId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
