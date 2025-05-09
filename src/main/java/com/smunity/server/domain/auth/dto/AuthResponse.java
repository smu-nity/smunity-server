package com.smunity.server.domain.auth.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String username,
        String name,
        String email,
        String department,
        String secondDepartment,
        String authToken
) {

}
