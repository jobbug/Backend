package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class BadRequestException extends JobbugException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
