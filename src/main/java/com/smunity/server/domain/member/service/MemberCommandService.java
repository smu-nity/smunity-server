package com.smunity.server.domain.member.service;

import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.domain.auth.service.AuthService;
import com.smunity.server.domain.member.dto.ChangeDepartmentRequestDto;
import com.smunity.server.domain.member.dto.ChangePasswordRequestDto;
import com.smunity.server.domain.member.dto.MemberInfoResponseDto;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.DepartmentRepository;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberInfoResponseDto updateMember(Long memberId, AuthRequestDto requestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        AuthResponseDto auth = authService.authenticate(requestDto);
        Department department = departmentRepository.findByName(auth.department())
                .orElseThrow(() -> new GeneralException(ErrorCode.DEPARTMENT_NOT_FOUND));
        member.update(department, auth.name(), auth.email());
        return MemberInfoResponseDto.from(member);
    }

    public MemberInfoResponseDto changePassword(Long memberId, ChangePasswordRequestDto requestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        String newEncodedPw = passwordEncoder.encode(requestDto.password());
        member.changePassword(newEncodedPw);
        return MemberInfoResponseDto.from(member);
    }

    public MemberInfoResponseDto changeDepartment(Long memberId, ChangeDepartmentRequestDto requestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getDepartment().isEditable())
            throw new GeneralException(ErrorCode.MEMBER_NOT_EDITABLE);
        Department department = departmentRepository.findById(requestDto.departmentId())
                .orElseThrow(() -> new GeneralException(ErrorCode.DEPARTMENT_NOT_FOUND));
        member.changeDepartment(department);
        return MemberInfoResponseDto.from(member);
    }

    public MemberInfoResponseDto changePasswordByAuth(String username, ChangePasswordRequestDto requestDto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return changePassword(member.getId(), requestDto);
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        member.delete();
    }
}
