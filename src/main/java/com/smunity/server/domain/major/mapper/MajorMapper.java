package com.smunity.server.domain.major.mapper;

import com.smunity.server.domain.major.dto.MajorResponseDto;
import com.smunity.server.domain.major.entity.Major;
import com.smunity.server.global.common.dto.ListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MajorMapper {

    MajorMapper INSTANCE = Mappers.getMapper(MajorMapper.class);

    @Mapping(target = "grade", source = "major.grade.name")
    @Mapping(target = "semester", source = "major.semester.name")
    MajorResponseDto toDto(Major major);

    default ListResponseDto<MajorResponseDto> toResponse(List<Major> majors) {
        return ListResponseDto.from(toDto(majors));
    }

    default List<MajorResponseDto> toDto(List<Major> majors) {
        return majors.stream()
                .map(this::toDto)
                .toList();
    }
}
