package com.fx.auth_server.common.exception.handler;

import com.fx.auth_server.common.exception.UserException;
import io.github.seob7.Api;
import io.github.seob7.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Api<BaseErrorCode>> handleUserException(UserException e) {
        log.error("", e);
        return Api.ERROR(e.getErrorCode());
    }

}