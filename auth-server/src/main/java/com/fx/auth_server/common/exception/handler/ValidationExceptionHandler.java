package com.fx.auth_server.common.exception.handler;

import com.fx.auth_server.common.exception.UserException;
import io.github.seob7.Api;
import io.github.seob7.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api<String>> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append("[");
            errorMessage.append(fieldError.getField());
            errorMessage.append("](은)는 ");
            errorMessage.append(fieldError.getDefaultMessage());
        }
        log.error("", e);
        return Api.ERROR(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

}