package com.smunity.server.global.common.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    ROLE_USER("회원"),
    ROLE_ADMIN("관리자"),
    ROLE_VERIFIED("인증된 회원");

    private final String description;
}
