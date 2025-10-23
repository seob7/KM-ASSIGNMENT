package com.fx.auth_server.adapter.out.session;

import com.fx.auth_server.application.port.out.TokenStoragePort;
import jakarta.servlet.http.HttpSession;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionTokenStorageAdapter implements TokenStoragePort {

    private static final String REFRESH_TOKEN = "refreshToken";

    @Value("${jwt.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    private final HttpSession httpSession;

    @Override
    public void save(String refreshToken) {
        httpSession.setAttribute(REFRESH_TOKEN, refreshToken);
        int expireSeconds = (int) Duration.ofHours(refreshTokenPlusHour).toSeconds(); // 시간 -> 초
        httpSession.setMaxInactiveInterval(expireSeconds);
    }

    @Override
    public String getToken() {
        Object token = httpSession.getAttribute(REFRESH_TOKEN);
        return token != null ? token.toString() : null;
    }

    @Override
    public void remove() {
        Object token = httpSession.getAttribute(REFRESH_TOKEN);
        System.out.println(token.toString());
        httpSession.invalidate();
    }

}
