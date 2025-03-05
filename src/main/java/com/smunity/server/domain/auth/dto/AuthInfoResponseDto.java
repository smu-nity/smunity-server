package com.smunity.server.domain.auth.dto;

import lombok.Builder;
import org.json.JSONObject;

import java.util.Map;

@Builder
public record AuthInfoResponseDto(
        String username,
        String name,
        String department,
        String email
) {

    private static final Map<String, String> DEPT_MAP = Map.of(
            "지능·데이터융합학부", "핀테크전공",
            "융합전자공학전공", "지능IOT융합전공"
    );

    public static AuthInfoResponseDto from(JSONObject obj) {
        return AuthInfoResponseDto.builder()
                .username(obj.getString("STDNO"))
                .name(obj.getString("NM_KOR"))
                .department(getDepartment(obj.getString("TMP_DEPT_MJR_NM")))
                .email(obj.getString("EMAIL"))
                .build();
    }

    private static String getDepartment(String dept) {
        String[] depts = dept.split(" ");
        String department = depts[depts.length - 1];
        return DEPT_MAP.getOrDefault(department, department);
    }
}
