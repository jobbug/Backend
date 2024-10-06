package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class ForbiddenException extends JobbugException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
