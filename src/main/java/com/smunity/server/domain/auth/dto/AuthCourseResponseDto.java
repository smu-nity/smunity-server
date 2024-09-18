package com.smunity.server.domain.auth.dto;

import lombok.Builder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

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
                .toList();
    }

    private static AuthCourseResponseDto from(JSONObject obj) {
        return AuthCourseResponseDto.builder()
                .number(obj.getString("SBJ_NO"))
                .name(obj.getString("SBJ_NM"))
                .type(obj.getString("CMP_DIV_NM"))
                .grade(obj.getString("GRD_NM"))
                .year(obj.getString("SCH_YEAR"))
                .semester(obj.getString("SMT_NM"))
                .domain(getDomain(obj.getString("CULT_ARA_NM")))
                .credit(obj.getInt("CDT"))
                .build();
    }

    private static String getDomain(String domain) {
        return !Objects.equals(domain, "*") ? domain : null;
    }
}
