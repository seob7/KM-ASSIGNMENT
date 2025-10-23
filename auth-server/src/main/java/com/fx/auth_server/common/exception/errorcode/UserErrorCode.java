package com.fx.auth_server.common.exception.errorcode;

import io.github.seob7.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements BaseErrorCode {

    EMAIL_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NICKNAME_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}