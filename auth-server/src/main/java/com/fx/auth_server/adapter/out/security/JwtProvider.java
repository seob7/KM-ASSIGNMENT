package com.fx.auth_server.adapter.out.security;

import com.fx.auth_server.application.port.out.JwtProviderPort;
import com.fx.auth_server.application.port.out.TokenStoragePort;
import com.fx.auth_server.common.exception.JwtException;
import com.fx.auth_server.common.exception.errorcode.JwtErrorCode;
import com.fx.auth_server.domain.AuthenticatedUserInfo;
import com.fx.auth_server.domain.TokenInfo;
import com.fx.auth_server.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
public class JwtProvider implements JwtProviderPort {

    private final TokenStoragePort tokenStoragePort;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${jwt.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    private SecretKey key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public TokenInfo generateTokens(Long userId, UserRole role) {
        String accessToken = createToken(userId, role, accessTokenPlusHour);
        String refreshToken = createToken(userId, role, refreshTokenPlusHour);
        return new TokenInfo(accessToken, refreshToken);
    }

    @Override
    public AuthenticatedUserInfo getAuthenticatedUserInfo(String accessToken) {
        Claims claims = parseTokenSafely(accessToken);
        Long userId = Long.parseLong(claims.get("userId").toString());
        UserRole role = UserRole.valueOf(claims.get("role").toString());
        return new AuthenticatedUserInfo(userId, role);
    }

    @Override
    public boolean validateToken(String token) {
        parseTokenSafely(token); // 예외 없으면 유효
        return true;
    }

    @Override
    public TokenInfo reIssueToken(String bearerRefreshToken) {
        if (!bearerRefreshToken.startsWith("Bearer")) {
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }
        String refreshToken = bearerRefreshToken.substring("Bearer".length()).trim();

        Claims claims = parseTokenSafely(refreshToken);

        String storedToken = tokenStoragePort.getToken();
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }

        Long userId = Long.parseLong(claims.get("userId").toString());
        UserRole role = UserRole.valueOf(claims.get("role").toString());

        String newAccessToken = createToken(userId, role, accessTokenPlusHour);

        return new TokenInfo(newAccessToken, refreshToken);
    }

    private Claims parseTokenSafely(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (SignatureException e) {
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new JwtException(JwtErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new JwtException(JwtErrorCode.TOKEN_EXCEPTION);
        }
    }

    private String createToken(Long userId, UserRole role, long expireHour) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("role", role.toString());

        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(expireHour);
        Date expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
            .signWith(key)
            .claims(claims)
            .expiration(expiredAt)
            .compact();
    }
}