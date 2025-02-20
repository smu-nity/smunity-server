package com.smunity.server.domain.auth.dto;

import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Builder
public record AuthCourseResponseDto(
        String number,
        String name,
        String type,
        String grade,
        String year,
        String semester,
        String domain,
        int credit
) {

    public static List<AuthCourseResponseDto> from(JSONArray objs) {
        return IntStream.range(0, objs.length())
                .mapToObj(i -> from(objs.getJSONObject(i)))
                .flatMap(Optional::stream)
                .toList();
    }

    private static Optional<AuthCourseResponseDto> from(JSONObject obj) {
        try {
            return Optional.of(of(obj));
        } catch (JSONException e) {
            log.error("[ERROR] Failed to convert JSON object: '{}'.", obj, e);
            return Optional.empty();
        }
    }

    private static AuthCourseResponseDto of(JSONObject obj) {
        return AuthCourseResponseDto.builder()
                .number(obj.getString("SBJ_NO"))
                .name(obj.getString("SBJ_NM"))
                .type(obj.getString("CMP_DIV_NM"))
                .grade(obj.getString("GRD_NM"))
                .year(obj.getString("SCH_YEAR"))
                .semester(obj.getString("SMT_NM"))
                .domain(obj.getString("CULT_ARA_NM"))
                .credit(obj.getInt("CDT"))
                .build();
    }

    public Course toEntity(boolean isNewCurriculum) {
        return Course.builder()
                .name(name)
                .number(number)
                .year(year)
                .semester(semester)
                .type(type)
                .domain(domain)
                .category(Category.of(type))
                .subDomain(SubDomain.of(domain, isNewCurriculum))
                .credit(credit)
                .build();
    }
}
