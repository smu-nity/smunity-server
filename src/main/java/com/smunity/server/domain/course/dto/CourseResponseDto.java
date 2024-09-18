package com.smunity.server.domain.course.dto;

import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.Builder;

import java.util.List;

@Builder
public record CourseResponseDto(
        Long id,
        String year,
        String semester,
        String number,
        String name,
        String type,
        String domain,
        Category category,
        SubDomain subDomain,
        int credit
) {

    private static CourseResponseDto from(Course course) {
        return CourseResponseDto.builder()
                .id(course.getId())
                .year(course.getYear())
                .semester(course.getSemester())
                .number(course.getNumber())
                .name(course.getName())
                .type(course.getType())
                .domain(course.getDomain())
                .category(course.getCategory())
                .subDomain(course.getSubDomain())
                .credit(course.getCredit())
                .build();
    }

    public static List<CourseResponseDto> from(List<Course> courses) {
        return courses.stream()
                .map(CourseResponseDto::from)
                .toList();
    }
}
