package com.smunity.server.global.common.mapper;

import com.smunity.server.global.common.dto.StatResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatResponseDto toResponse(long totalMembers, long newRegisters, long activeMembers, long unansweredQuestions);
}
