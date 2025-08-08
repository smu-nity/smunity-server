package com.smunity.server.domain.member.service;

import com.smunity.server.domain.auth.dto.AuthDto;
import com.smunity.server.domain.auth.dto.AuthRequest;
import com.smunity.server.domain.auth.service.AuthService;
import com.smunity.server.domain.department.service.DepartmentService;
import com.smunity.server.domain.member.dto.ChangeDepartmentRequest;
import com.smunity.server.domain.member.dto.ChangeExemptionRequest;
import com.smunity.server.domain.member.dto.ChangePasswordRequest;
import com.smunity.server.domain.member.dto.MemberInfoResponse;
import com.smunity.server.domain.member.mapper.MemberMapper;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
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
    private final DepartmentService departmentService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public MemberInfoResponse updateMember(Long memberId, AuthRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        AuthDto dto = authService.authenticate(request);
        Department department = departmentService.findDepartmentByName(dto.department());
        Department secondDepartment = departmentService.findDepartmentByName(dto.secondDepartment());
        member.update(department, secondDepartment, dto.name(), dto.email());
        return memberMapper.toResponse(member);
    }

    public MemberInfoResponse changePassword(Long memberId, ChangePasswordRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        String newEncodedPw = passwordEncoder.encode(request.password());
        member.changePassword(newEncodedPw);
        return memberMapper.toResponse(member);
    }

    public MemberInfoResponse changeDepartment(Long memberId, ChangeDepartmentRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getDepartment().isEditable())
            throw new GeneralException(ErrorCode.MEMBER_NOT_EDITABLE);
        Department department = departmentService.findDepartmentById(request.departmentId());
        member.changeDepartment(department);
        return memberMapper.toResponse(member);
    }

    public MemberInfoResponse changeExemption(Long memberId, ChangeExemptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        member.changeExemption(request.exemption());
        return memberMapper.toResponse(member);
    }

    public MemberInfoResponse changePasswordByAuth(String username, ChangePasswordRequest request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return changePassword(member.getId(), request);
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
