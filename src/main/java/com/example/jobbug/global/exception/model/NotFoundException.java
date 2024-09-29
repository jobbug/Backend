package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class NotFoundException extends JobbugException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
