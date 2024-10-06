package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class DuplicateException extends JobbugException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
