package com.smunity.server.domain.account.mapper;

import com.smunity.server.domain.account.dto.RegisterRequestDto;
import com.smunity.server.domain.account.dto.RegisterResponseDto;
import com.smunity.server.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    RegisterResponseDto toDto(Member member);

    @Mapping(target = "department", ignore = true)
    Member toEntity(RegisterRequestDto requestDto);
}
