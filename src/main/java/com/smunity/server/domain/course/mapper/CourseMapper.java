package com.smunity.server.domain.course.mapper;

import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.CreditResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.dto.StatusResponseDto;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.smunity.server.global.common.entity.enums.Category.*;

@Mapper
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponseDto toDto(Course course);

    default List<CourseResponseDto> toDto(List<Course> courses) {
        return courses.stream()
                .map(this::toDto)
                .toList();
    }

    default CreditResponseDto toDto(int total, Member member) {
        int completed = member.getCompletedCredits();
        int major = member.getCompletedCredits(MAJOR_ADVANCED) + member.getCompletedCredits(MAJOR_OPTIONAL);
        int culture = member.getCompletedCredits(CULTURE);
        return CreditResponseDto.builder()
                .username(member.getUsername())
                .name(member.getName())
                .total(total)
                .completed(completed)
                .major(major)
                .culture(culture)
                .etc(completed - major - culture)
                .required(calculateRequired(total, completed))
                .completion(calculateCompletion(total, completed))
                .build();
    }

    default <T> ResultResponseDto<T> toDto(int total, int completed, List<T> responses) {
        return ResultResponseDto.<T>builder()
                .completed(total <= completed)
                .status(toDto(total, completed))
                .count(responses.size())
                .content(responses)
                .build();
    }

    default StatusResponseDto toDto(int total, int completed) {
        return StatusResponseDto.builder()
                .total(total)
                .completed(completed)
                .required(calculateRequired(total, completed))
                .completion(calculateCompletion(total, completed))
                .build();
    }

    default int calculateRequired(int total, int completed) {
        return Math.max(0, total - completed);
    }

    default int calculateCompletion(int total, int completed) {
        return total != 0 ? Math.min(100, completed * 100 / total) : 100;
    }
}
