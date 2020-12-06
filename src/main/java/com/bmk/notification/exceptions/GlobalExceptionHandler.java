package com.bmk.notification.exceptions;

import com.bmk.notification.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity exceptionHandler(AuthorizationException e) {
        logger.info(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("400", e.getMessage()));
    }
}