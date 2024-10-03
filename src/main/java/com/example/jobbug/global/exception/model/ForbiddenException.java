package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class ForbiddenException extends JobbugException {
    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN_EXCEPTION);
    }

    public ForbiddenException(String message) {
        super(ErrorCode.FORBIDDEN_EXCEPTION, message);
    }
}
