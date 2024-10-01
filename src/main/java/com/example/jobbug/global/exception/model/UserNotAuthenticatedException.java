package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class UserNotAuthenticatedException extends JobbugException {
    public UserNotAuthenticatedException() {
        super(ErrorCode.UNAUTHORIZED_EXCEPTION);
    }
}
