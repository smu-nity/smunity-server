package com.smunity.server.domain.culture.mapper;

import com.smunity.server.domain.culture.dto.CultureResponse;
import com.smunity.server.domain.culture.entity.Culture;
import com.smunity.server.global.common.dto.ListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CultureMapper {

    @Mapping(target = "type", source = "culture.subDomain.name")
    CultureResponse toResponse(Culture culture);

    default List<CultureResponse> toListResponse(List<Culture> cultures) {
        return cultures.stream()
                .map(this::toResponse)
                .toList();
    }

    default ListResponse<CultureResponse> toResponse(List<Culture> cultures) {
        return ListResponse.from(toListResponse(cultures));
    }
}
