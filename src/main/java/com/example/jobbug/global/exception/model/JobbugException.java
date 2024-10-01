package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class JobbugException extends RuntimeException{
    private final ErrorCode errorCode;

    public JobbugException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JobbugException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
