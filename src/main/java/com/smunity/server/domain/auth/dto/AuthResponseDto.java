package com.smunity.server.domain.auth.dto;

import lombok.Builder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

@Builder
public record AuthResponseDto(
        String username,
        String name,
        String department,
        String email,
        String authToken
) {

    private static final Map<String, String> DEPT_MAP = Map.of(
            "지능·데이터융합학부", "핀테크전공",
            "융합전자공학전공", "지능IOT융합전공"
    );

    public static AuthResponseDto of(JSONArray json, String authToken) {
        return of(json.getJSONObject(0), authToken);
    }

    private static AuthResponseDto of(JSONObject obj, String authToken) {
        return AuthResponseDto.builder()
                .username(obj.getString("STDNO"))
                .name(obj.getString("NM_KOR"))
                .department(getDepartment(obj.getString("TMP_DEPT_MJR_NM")))
                .email(obj.getString("EMAIL"))
                .authToken(authToken)
                .build();
    }

    private static String getDepartment(String dept) {
        String[] depts = dept.split(" ");
        String department = depts[depts.length - 1];
        return DEPT_MAP.getOrDefault(department, department);
    }
}
