package com.fx.auth_server.common.exception.handler;

import com.fx.auth_server.common.exception.JwtException;
import io.github.seob7.Api;
import io.github.seob7.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Api<BaseErrorCode>> handleJwtException(JwtException e) {
        log.error("", e);
        return Api.ERROR(e.getErrorCode());
    }

}