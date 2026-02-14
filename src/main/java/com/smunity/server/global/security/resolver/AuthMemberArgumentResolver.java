package com.smunity.server.global.security.resolver;

import com.smunity.server.global.security.annotation.AuthMember;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러 메서드의 파라미터가 @AuthMember Member 타입일 때 해당 파라미터를 처리하도록 지정하는 클래스
 */
@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 파라미터 타입 확인 (@AuthMember, Member)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthMemberAnnotation = parameter.hasParameterAnnotation(AuthMember.class);
        boolean isLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthMemberAnnotation && isLongType;
    }

    /**
     * 해당 컨트롤러 메서드의 파라미터 처리
     */
    @Override
    public Long resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(authentication.getName());
    }
}
