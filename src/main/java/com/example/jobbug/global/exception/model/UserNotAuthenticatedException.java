package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class UserNotAuthenticatedException extends JobbugException {
    public UserNotAuthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
