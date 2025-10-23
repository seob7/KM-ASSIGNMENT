package com.fx.auth_server.common.resolver;

import com.fx.auth_server.common.annotation.AuthenticatedUser;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean annotation = parameter.hasParameterAnnotation(AuthenticatedUser.class);

        boolean parameterType = parameter.getParameterType().equals(AuthUser.class);

        return (annotation && parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // supportsParameter true 시 실행

        boolean required = parameter.getParameterAnnotation(AuthenticatedUser.class).required();

        // request context holder 에서 user id 찾기
        RequestAttributes requestContext = Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes());

        Object userId = requestContext.getAttribute("x-user-id",
            RequestAttributes.SCOPE_REQUEST);

        if (userId == null && required) {
            throw new RuntimeException("서버 에러");
        }

        return (userId != null) ? new AuthUser(Long.parseLong(userId.toString())) : null;
    }
}
