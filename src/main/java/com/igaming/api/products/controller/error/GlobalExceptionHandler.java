package com.igaming.api.products.controller.error;

import com.igaming.api.products.exception.NotFoundProductException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException exception) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(NotFoundProductException.class)
    public void handleNotFoundProductException(NotFoundProductException exception) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
    }
}
