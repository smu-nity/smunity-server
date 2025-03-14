package com.smunity.server.domain.account.mapper;

import com.smunity.server.domain.account.dto.LoginResponse;
import com.smunity.server.domain.account.dto.RegisterRequest;
import com.smunity.server.domain.account.dto.RegisterResponse;
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

    RegisterResponse toResponse(Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "exemption", ignore = true)
    @Mapping(target = "department", ignore = true)
    Member toEntity(RegisterRequest request);

    default LoginResponse toResponse(String username, MemberRole memberRole, String accessToken, String refreshToken) {
        return new LoginResponse(username, memberRole, accessToken, refreshToken);
    }

    default LoginStatus toEntity(HttpServletRequest servletRequest) {
        return LoginStatus.builder()
                .ipAddress(IpUtil.getClientIp(servletRequest))
                .build();
    }
}
