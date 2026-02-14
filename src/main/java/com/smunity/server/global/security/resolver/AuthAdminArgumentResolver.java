package com.smunity.server.global.security.resolver;

import com.smunity.server.global.security.annotation.AuthAdmin;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import com.smunity.server.global.security.util.PermissionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Authentication authentication = jwtTokenProvider.getAuthentication(request);
        return authentication != null ? PermissionUtil.isAdmin(authentication.getAuthorities()) : null;
    }
}
