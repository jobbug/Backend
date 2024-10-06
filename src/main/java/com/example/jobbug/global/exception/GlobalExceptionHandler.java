package com.example.jobbug.global.exception;

import com.example.jobbug.global.dto.ApiResponse;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.JobbugException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobbugException.class)
    public ResponseEntity<?> handleJobbugException(JobbugException e) {
        return ErrorResponse.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        e.printStackTrace();
        return ErrorResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }
}
