package com.smunity.server.global.security.util;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static com.smunity.server.global.common.entity.enums.MemberRole.ROLE_ADMIN;

public class PermissionUtil {

    // 주어진 회원 ID(memberId), 관리자인지 여부(isAdmin), 작성자 ID(authorId)를 기준으로 권한 검증
    public static void validatePermission(Long memberId, Boolean isAdmin, Long authorId) {
        if (!checkPermission(memberId, isAdmin, authorId)) {
            throw new GeneralException(ErrorCode.MEMBER_FORBIDDEN);
        }
    }

    // 회원 ID(memberId)와 작성자 ID(authorId)를 비교하고 관리자인지 여부(isAdmin)를 검사하여 권한 확인
    public static boolean checkPermission(Long memberId, boolean isAdmin, Long authorId) {
        return memberId.equals(authorId) || isAdmin;
    }

    // 주어진 권한 목록에 관리자 권한이 포함되어 있는지 확인
    public static boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_ADMIN.name()::equals);
    }
}
