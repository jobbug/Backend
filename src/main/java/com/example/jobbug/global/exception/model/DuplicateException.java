package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class DuplicateException extends JobbugException {

    public DuplicateException() {
        super(ErrorCode.DUPLICATE_EXCEPTION);
    }

    public DuplicateException(String message) {
        super(ErrorCode.DUPLICATE_EXCEPTION, message);
    }
}
