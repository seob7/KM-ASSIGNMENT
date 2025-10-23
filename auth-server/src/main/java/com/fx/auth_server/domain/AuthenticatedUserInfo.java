package com.fx.auth_server.domain;

/**
 * 인증된 사용자의 기본 정보를 담는 도메인 객체

 * @param userId 인증된 사용자의 고유 식별자
 * @param role 사용자의 권한 역할
 * 
 * @author SEOB
 * @since 2025-10-22
 */
public record AuthenticatedUserInfo(
    Long userId,
    UserRole role
) {

}
