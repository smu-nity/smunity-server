package com.smunity.server.domain.account.mapper;

import com.smunity.server.domain.account.dto.LoginResponseDto;
import com.smunity.server.domain.account.dto.RegisterRequestDto;
import com.smunity.server.domain.account.dto.RegisterResponseDto;
import com.smunity.server.domain.account.entity.LoginStatus;
import com.smunity.server.domain.account.util.IpUtil;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    RegisterResponseDto toDto(Member member);

    @Mapping(target = "department", ignore = true)
    Member toEntity(RegisterRequestDto requestDto);

    default LoginResponseDto toDto(String username, MemberRole memberRole, String accessToken, String refreshToken) {
        return new LoginResponseDto(username, memberRole, accessToken, refreshToken);
    }

    default LoginStatus toEntity(HttpServletRequest request) {
        return LoginStatus.builder()
                .ipAddress(IpUtil.getClientIp(request))
                .build();
    }
}
