package com.kirillpolyakov.controller;

import com.kirillpolyakov.dto.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseResult<Object>> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ResponseResult<>(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
