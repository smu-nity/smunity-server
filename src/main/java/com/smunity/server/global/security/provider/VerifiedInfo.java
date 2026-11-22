package com.smunity.server.global.security.provider;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 학교 인증을 통과한 사용자 정보 (AUTH 토큰 클레임으로 전달)
 */
@Builder
public record VerifiedInfo(
        String username,
        String name,
        String email,
        String department,
        String secondDepartment,
        boolean teachingTrack
) {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_DEPARTMENT = "department";
    private static final String CLAIM_SECOND_DEPARTMENT = "secondDepartment";
    private static final String CLAIM_TEACHING_TRACK = "teachingTrack";

    /**
     * 토큰 클레임으로 변환 (학번은 subject로 전달하므로 제외, null 값은 제외)
     */
    public Map<String, Object> toClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_NAME, name);
        claims.put(CLAIM_EMAIL, email);
        claims.put(CLAIM_DEPARTMENT, department);
        claims.put(CLAIM_SECOND_DEPARTMENT, secondDepartment);
        claims.put(CLAIM_TEACHING_TRACK, teachingTrack);
        claims.values().removeIf(Objects::isNull);
        return claims;
    }

    /**
     * 토큰 클레임에서 복원 (회원가입용 클레임이 없으면 null)
     */
    public static VerifiedInfo of(String username, Map<String, ?> claims) {
        if (claims.get(CLAIM_DEPARTMENT) == null) {
            return null;
        }
        return VerifiedInfo.builder()
                .username(username)
                .name((String) claims.get(CLAIM_NAME))
                .email((String) claims.get(CLAIM_EMAIL))
                .department((String) claims.get(CLAIM_DEPARTMENT))
                .secondDepartment((String) claims.get(CLAIM_SECOND_DEPARTMENT))
                .teachingTrack(Boolean.TRUE.equals(claims.get(CLAIM_TEACHING_TRACK)))
                .build();
    }
}
