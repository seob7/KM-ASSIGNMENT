package com.fx.auth_server.application.port.out;


import com.fx.auth_server.domain.AuthenticatedUserInfo;
import com.fx.auth_server.domain.TokenInfo;
import com.fx.auth_server.domain.UserRole;

public interface JwtProviderPort {

    /**
     * access token 과 refresh token 을 생성.
     *
     * @param userId user domain
     * @param role user domain
     * @return TokenInfo(accessToken, refreshToken)
     */
    TokenInfo generateTokens(Long userId, UserRole role);

    /**
     * 토큰으로부터 인증된 사용자 정보를 조회합니다.
     *
     * @param accessToken access token
     * @return AuthenticatedUserInfo
     */
    AuthenticatedUserInfo getAuthenticatedUserInfo(String accessToken);

    /**
     * 토큰 유효성을 검증.
     *
     * @param token access or refresh token
     * @return 유효하면 true, 아니면 예외 발생
     */
    boolean validateToken(String token);

    TokenInfo reIssueToken(String bearerRefreshToken);

}