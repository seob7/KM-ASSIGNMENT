package com.fx.auth_server.common.exception;

import io.github.seob7.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public UserException(BaseErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}