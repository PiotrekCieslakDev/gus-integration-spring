package com.piotrekcieslak.gusintegrationspring.controller;

import com.piotrekcieslak.gusintegrationspring.ApiError;
import com.piotrekcieslak.gusintegrationspring.exception.GusAuthException;
import com.piotrekcieslak.gusintegrationspring.exception.GusException;
import com.piotrekcieslak.gusintegrationspring.exception.GusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GusExceptionHandler {

    @ExceptionHandler({
            GusException.class,
            GusAuthException.class
    })
    public ResponseEntity<ApiError> handleGusException(GusException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(GusNotFoundException.class)
    public ResponseEntity<ApiError> handleGusNotFoundException(GusNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> 
            errors.put(err.getField(), err.getDefaultMessage()));

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Podane parametry są nieprawidłowe")
                .path(request.getRequestURI())
                .validationErrors(errors)
                .build();
        return ResponseEntity.badRequest().body(apiError);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String msg, HttpServletRequest request) {
        ApiError err = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(msg)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(err, status);
    }
}