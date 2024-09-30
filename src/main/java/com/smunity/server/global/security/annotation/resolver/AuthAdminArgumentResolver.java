package com.smunity.server.global.security.annotation.resolver;

import com.smunity.server.global.security.annotation.AuthAdmin;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;

import static com.smunity.server.global.common.entity.enums.MemberRole.ROLE_ADMIN;

/**
 * 컨트롤러 메서드의 파라미터가 @AuthAdmin Boolean 타입일 때 해당 파라미터를 처리하도록 지정하는 클래스
 */
@Component
@RequiredArgsConstructor
public class AuthAdminArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 파라미터 타입 확인 (@AuthAdmin, Boolean)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAdminAnnotation = parameter.hasParameterAnnotation(AuthAdmin.class);
        boolean isBooleanType = Boolean.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthAdminAnnotation && isBooleanType;
    }

    /**
     * 해당 컨트롤러 메서드의 파라미터 처리
     */
    @Override
    public Boolean resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String jwt = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.validateToken(jwt, false) ?
                isAdmin(jwtTokenProvider.getAuthentication(jwt).getAuthorities()) : null;
    }

    // 주어진 권한 목록에 관리자 권한이 포함되어 있는지 확인
    private boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_ADMIN.name()::equals);
    }
}
