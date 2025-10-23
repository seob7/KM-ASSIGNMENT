package com.fx.auth_server.common.interceptor;

import com.fx.auth_server.application.port.out.JwtProviderPort;
import com.fx.auth_server.domain.AuthenticatedUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String X_USER_ID = "x-user-id";
    public static final String X_USER_ROLE = "x-user-role";

    private final JwtProviderPort jwtProviderPort;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //Authorization Header 추출
        String accessToken = request.getHeader(AUTHORIZATION);

        AuthenticatedUserInfo userInfo = jwtProviderPort.getAuthenticatedUserInfo(accessToken.substring(7));

        RequestAttributes requestContext = Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute(X_USER_ID, userInfo.userId(),
            RequestAttributes.SCOPE_REQUEST);
        requestContext.setAttribute(X_USER_ROLE, userInfo.role(),
            RequestAttributes.SCOPE_REQUEST);

        return true;
        }

}
