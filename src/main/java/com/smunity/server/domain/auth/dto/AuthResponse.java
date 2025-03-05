package com.smunity.server.domain.auth.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String username,
        String name,
        String department,
        String email,
        String authToken
) {

}
