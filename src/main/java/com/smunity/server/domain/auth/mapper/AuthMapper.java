package com.smunity.server.domain.auth.mapper;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthResponse;
import com.smunity.server.domain.auth.dto.AuthResponseDto;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static com.smunity.server.global.common.entity.enums.Category.CULTURE;
import static com.smunity.server.global.common.entity.enums.Category.MAJOR_OPTIONAL;
import static com.smunity.server.global.common.entity.enums.SubDomain.BALANCE_NATURAL_ENGINEER;
import static com.smunity.server.global.common.entity.enums.SubDomain.BASIC_ENG_MATH;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    @Mapping(target = "category", expression = "java(of(dto.type()))")
    @Mapping(target = "subDomain", expression = "java(of(dto.domain(), isNewCurriculum))")
    Course toEntity(AuthCourseResponseDto dto, boolean isNewCurriculum);

    AuthResponse toResponse(AuthResponseDto dto, String authToken);

    default AuthResponse toResponse(JSONObject obj, String authToken) {
        return toResponse(AuthResponseDto.from(obj), authToken);
    }

    default List<AuthCourseResponseDto> toDto(JSONArray objs) {
        return AuthCourseResponseDto.from(objs);
    }

    default Category of(String name) {
        Map<String, Category> categoryMap = Map.of("교필", CULTURE, "교선", CULTURE, "1교직", MAJOR_OPTIONAL);
        return categoryMap.getOrDefault(name, Category.findByName(name));
    }

    default SubDomain of(String name, boolean isNewCurriculum) {
        SubDomain subDomain = SubDomain.hasEngMath(name) ? BASIC_ENG_MATH : SubDomain.findByName(name);
        return isNewCurriculum && subDomain != null && subDomain.isNaturalOrEngineer() ? BALANCE_NATURAL_ENGINEER : subDomain;
    }
}
