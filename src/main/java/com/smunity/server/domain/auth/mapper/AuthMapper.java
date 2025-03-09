package com.smunity.server.domain.auth.mapper;

import com.smunity.dto.AuthCourseResponseDto;
import com.smunity.dto.AuthResponseDto;
import com.smunity.server.domain.auth.dto.AuthResponse;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    @Mapping(target = "category", expression = "java(of(dto.type()))")
    @Mapping(target = "subDomain", expression = "java(of(dto.domain(), isNewCurriculum))")
    Course toEntity(AuthCourseResponseDto dto, boolean isNewCurriculum);

    AuthResponse toResponse(AuthResponseDto dto, String authToken);

    default Category of(String name) {
        return Category.of(name);
    }

    default SubDomain of(String name, boolean isNewCurriculum) {
        return SubDomain.of(name, isNewCurriculum);
    }
}
