package com.smunity.server.domain.course.mapper;

import com.smunity.server.domain.course.dto.CourseResponse;
import com.smunity.server.domain.course.dto.CreditResponse;
import com.smunity.server.domain.course.dto.ResultResponse;
import com.smunity.server.domain.course.dto.StatusResponse;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.smunity.server.global.common.entity.enums.Category.CULTURE;
import static com.smunity.server.global.common.entity.enums.Category.ETC;

@Mapper
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponse toResponse(Course course);

    default List<CourseResponse> toResponse(List<Course> courses) {
        return courses.stream()
                .map(this::toResponse)
                .toList();
    }

    default CreditResponse toResponse(int total, Member member) {
        int completed = member.getCompletedCredits();
        return CreditResponse.builder()
                .username(member.getUsername())
                .name(member.getName())
                .total(total)
                .completed(completed)
                .major(member.getCompletedMajorCredits())
                .secondMajor(member.getCompletedSecondMajorCredits())
                .culture(member.getCompletedCredits(CULTURE))
                .etc(member.getCompletedCredits(ETC))
                .required(calculateRequired(total, completed))
                .completion(calculateCompletion(total, completed))
                .build();
    }

    default <T> ResultResponse<T> toResponse(int total, int completed, List<T> responses) {
        return ResultResponse.<T>builder()
                .completed(total <= completed)
                .status(toResponse(total, completed))
                .count(responses.size())
                .content(responses)
                .build();
    }

    default StatusResponse toResponse(int total, int completed) {
        return StatusResponse.builder()
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
