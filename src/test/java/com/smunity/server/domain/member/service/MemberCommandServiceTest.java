package com.smunity.server.domain.member.service;

import com.smunity.server.domain.account.service.RefreshTokenService;
import com.smunity.server.domain.auth.service.AuthService;
import com.smunity.server.domain.department.service.DepartmentService;
import com.smunity.server.domain.member.dto.ChangePasswordRequest;
import com.smunity.server.domain.member.mapper.MemberMapper;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberCommandServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private AuthService authService;
    @Mock
    private DepartmentService departmentService;
    @InjectMocks
    private MemberCommandService service;

    @Test
    void 비밀번호를_변경하면_refresh_토큰을_모두_폐기한다() {
        Member member = mock(Member.class);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded");

        service.changePassword(1L, new ChangePasswordRequest("new-password"));

        verify(member).changePassword("encoded");
        verify(refreshTokenService).deleteAllByMemberId(1L);
    }

    @Test
    void 인증_토큰으로_비밀번호를_재설정해도_refresh_토큰을_모두_폐기한다() {
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(memberRepository.findByUsername("202012345")).thenReturn(Optional.of(member));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded");

        service.changePasswordByAuth("202012345", new ChangePasswordRequest("new-password"));

        verify(refreshTokenService).deleteAllByMemberId(1L);
    }

    @Test
    void 회원을_삭제하면_refresh_토큰을_모두_폐기한다() {
        Member member = mock(Member.class);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        service.deleteMember(1L);

        verify(memberRepository).delete(member);
        verify(refreshTokenService).deleteAllByMemberId(1L);
    }
}
