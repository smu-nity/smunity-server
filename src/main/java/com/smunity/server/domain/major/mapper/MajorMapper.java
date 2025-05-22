package com.smunity.server.domain.major.mapper;

import com.smunity.server.domain.major.dto.MajorResponse;
import com.smunity.server.domain.major.entity.Major;
import com.smunity.server.global.common.dto.ListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    @Mapping(target = "grade", source = "major.grade.name")
    @Mapping(target = "semester", source = "major.semester.name")
    MajorResponse toResponse(Major major);

    default ListResponse<MajorResponse> toResponse(List<Major> majors) {
        return ListResponse.from(toListResponse(majors));
    }

    default List<MajorResponse> toListResponse(List<Major> majors) {
        return majors.stream()
                .map(this::toResponse)
                .toList();
    }
}
