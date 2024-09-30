package com.smunity.server.global.validation.validator;

import com.smunity.server.global.security.util.PermissionUtil;
import com.smunity.server.global.validation.annotation.PermissionCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
        if (authentication == null || !checkPermission(value, authentication)) {
            addViolation(context);
            return false;
        }
        return true;
    }

    // 인증 정보를 바탕으로 권한 확인
    private boolean checkPermission(Long value, Authentication authentication) {
        Long memberId = Long.valueOf(authentication.getName());
        boolean isAdmin = PermissionUtil.isAdmin(authentication.getAuthorities());
        return PermissionUtil.checkPermission(memberId, isAdmin, value);
    }

    // 유효성 검사 실패 시 위반 사항을 추가 (ErrorCode 이름)
    private void addViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(MEMBER_FORBIDDEN.name())
                .addConstraintViolation();
    }
}
