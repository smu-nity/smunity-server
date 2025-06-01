package com.smunity.server.global.common.mapper;

import com.smunity.server.global.common.dto.StatResponseDto;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatResponseDto toResponse(LocalDate date, long totalMembers, long newRegisters, long activeMembers, long unansweredQuestions);
}
