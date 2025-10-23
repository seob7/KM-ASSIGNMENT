package com.fx.auth_server.domain;

/**
 * JWT 토큰 정보를 담는 도메인 객체
 *
 * @param accessToken API 요청 인증에 사용되는 액세스 토큰 (짧은 수명)
 * @param refreshToken 액세스 토큰 갱신에 사용되는 리프레시 토큰 (긴 수명)
 * 
 * @author SEOB
 * @since 2025-10-22
 */
public record TokenInfo(
    String accessToken,
    String refreshToken
) {

}