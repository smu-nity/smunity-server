package com.smunity.server.global.security.resolver;

import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import com.smunity.server.global.security.annotation.AuthVerified;
import com.smunity.server.global.security.provider.JwtTokenProvider;
import com.smunity.server.global.security.provider.VerifiedInfo;
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
 * 컨트롤러 메서드의 파라미터가 @AuthVerified String 타입일 때 해당 파라미터를 처리하도록 지정하는 클래스
 */
@Component
@RequiredArgsConstructor
public class AuthVerifiedArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 파라미터 타입 확인 (@AuthVerified, String)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthMemberAnnotation = parameter.hasParameterAnnotation(AuthVerified.class);
        Class<?> type = parameter.getParameterType();
        boolean isSupportedType = String.class.isAssignableFrom(type) || VerifiedInfo.class.isAssignableFrom(type);
        return hasAuthMemberAnnotation && isSupportedType;
    }

    /**
     * 해당 컨트롤러 메서드의 파라미터 처리
     */
    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!VerifiedInfo.class.isAssignableFrom(parameter.getParameterType())) {
            return authentication.getName();
        }
        VerifiedInfo info = jwtTokenProvider.getVerifiedInfo(authentication);
        if (info == null) {
            throw new GeneralException(ErrorCode.UNVERIFIED_USER);
        }
        return info;
    }
}
