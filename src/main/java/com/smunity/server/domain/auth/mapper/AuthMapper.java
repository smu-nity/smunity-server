package com.smunity.server.domain.auth.mapper;

import com.smunity.dto.AuthResponseDto;
import com.smunity.dto.CourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthDto;
import com.smunity.server.domain.auth.dto.AuthResponse;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    Map<String, String> DEPT_MAP = Map.of(
            "지능·데이터융합학부", "핀테크전공",
            "융합전자공학전공", "지능IOT융합전공"
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "category", expression = "java(category(dto.type(), isDoubleMajor))")
    @Mapping(target = "subDomain", expression = "java(subDomain(dto.domain(), isNewCurriculum))")
    Course toEntity(CourseResponseDto dto, boolean isDoubleMajor, boolean isNewCurriculum);

    AuthResponse toResponse(AuthDto dto, String authToken);

    @Mapping(target = "department", qualifiedByName = "mapDepartment")
    @Mapping(target = "secondDepartment", qualifiedByName = "mapDepartment")
    AuthDto toDto(AuthResponseDto dto);

    default Category category(String name, boolean isDoubleMajor) {
        return Category.of(name, isDoubleMajor);
    }

    default SubDomain subDomain(String name, boolean isNewCurriculum) {
        return SubDomain.of(name, isNewCurriculum);
    }

    @Named("mapDepartment")
    default String mapDepartment(String name) {
        return name != null ? DEPT_MAP.getOrDefault(name, name) : null;
    }
}
