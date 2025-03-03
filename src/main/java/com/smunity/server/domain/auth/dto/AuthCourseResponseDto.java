package com.smunity.server.domain.auth.dto;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

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

    public static Optional<AuthCourseResponseDto> from(JSONObject obj) {
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
}
