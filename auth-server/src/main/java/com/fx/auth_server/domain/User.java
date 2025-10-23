package com.fx.auth_server.domain;

import com.fx.auth_server.application.port.in.dto.UserRegisterCommand;

/**
 * 사용자 도메인 엔티티
 * 
 * @param id 사용자 고유 식별자 (데이터베이스에서 자동 생성)
 * @param email 사용자 이메일 주소 (로그인 ID로 사용)
 * @param password 암호화된 비밀번호
 * @param nickname 사용자 닉네임
 * @param role 사용자 권한 역할
 * 
 * @author SEOB
 * @since 2025-10-22
 */
public record User(
    Long id,
    String email,
    String password,
    String nickname,
    UserRole role
) {

    public static User registerUser(
        UserRegisterCommand userRegisterCommand,
        String encodedPassword
    ) {
        return new User(
            null,
            userRegisterCommand.email(),
            encodedPassword,
            userRegisterCommand.nickname(),
            UserRole.USER
        );
    }

}
