package com.smunity.server.global.validation.validator;

import com.smunity.server.global.validation.annotation.PermissionCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.smunity.server.global.common.entity.enums.MemberRole.ROLE_ADMIN;
import static com.smunity.server.global.exception.code.ErrorCode.MEMBER_FORBIDDEN;


/**
 * PermissionCheck 어노테이션에 대한 유효성 검사를 수행하는 Validator 클래스
 */
@Component
@RequiredArgsConstructor
public class PermissionCheckValidator implements ConstraintValidator<PermissionCheck, Long> {

    /**
     * 주어진 값에 대한 권한 검사를 수행
     *
     * @return 권한이 있으면 true, 없으면 false (HandlerMethodValidationException)
     */
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !checkPermission(value.toString(), authentication)) {
            addViolation(context);
            return false;
        }
        return true;
    }

    // 인증 정보를 바탕으로 권한 확인
    private boolean checkPermission(String value, Authentication authentication) {
        return isSameMember(value, authentication.getName()) || isAdmin(authentication.getAuthorities());
    }

    // 주어진 값이 현재 인증된 멤버의 ID와 일치하는지 확인
    private boolean isSameMember(String value, String memberId) {
        return value.equals(memberId);
    }

    // 주어진 권한 목록에 관리자 권한이 포함되어 있는지 확인
    private boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_ADMIN.name()::equals);
    }

    // 유효성 검사 실패 시 위반 사항을 추가 (ErrorCode 이름)
    private void addViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(MEMBER_FORBIDDEN.name())
                .addConstraintViolation();
    }
}
