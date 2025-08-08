package com.smunity.server.domain.auth.mapper;

import com.smunity.dto.AuthCourseResponseDto;
import com.smunity.dto.AuthResponseDto;
import com.smunity.server.domain.auth.dto.AuthDto;
import com.smunity.server.domain.auth.dto.AuthResponse;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "category", expression = "java(category(dto.type(), isDoubleMajor))")
    @Mapping(target = "subDomain", expression = "java(subDomain(dto.domain(), isNewCurriculum))")
    Course toEntity(AuthCourseResponseDto dto, boolean isDoubleMajor, boolean isNewCurriculum);

    AuthResponse toResponse(AuthDto dto, String authToken);

    AuthDto toDto(AuthResponseDto dto);

    default Category category(String name, boolean isDoubleMajor) {
        return Category.of(name, isDoubleMajor);
    }

    default SubDomain subDomain(String name, boolean isNewCurriculum) {
        return SubDomain.of(name, isNewCurriculum);
    }
}
