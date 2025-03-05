package com.smunity.server.domain.culture.mapper;

import com.smunity.server.domain.culture.dto.CultureResponseDto;
import com.smunity.server.domain.culture.entity.Culture;
import com.smunity.server.global.common.dto.ListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CultureMapper {

    CultureMapper INSTANCE = Mappers.getMapper(CultureMapper.class);

    @Mapping(target = "type", source = "culture.subDomain.name")
    CultureResponseDto toDto(Culture culture);

    default List<CultureResponseDto> toDto(List<Culture> cultures) {
        return cultures.stream()
                .map(this::toDto)
                .toList();
    }

    default ListResponseDto<CultureResponseDto> toResponse(List<Culture> cultures) {
        return ListResponseDto.from(toDto(cultures));
    }
}
