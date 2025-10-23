package com.fx.auth_server.common.exception.errorcode;

import io.github.seob7.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum JwtErrorCode implements BaseErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "알 수 없는 토큰 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    JwtErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}