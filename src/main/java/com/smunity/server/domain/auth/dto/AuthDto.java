package com.smunity.server.domain.auth.dto;

import lombok.Builder;

@Builder
public record AuthDto(
        String username,
        String name,
        String email,
        String department,
        String secondDepartment
) {

}
