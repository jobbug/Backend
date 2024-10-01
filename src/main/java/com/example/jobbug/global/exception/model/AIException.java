package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class AIException extends JobbugException {
    public AIException(ErrorCode errorCode) {
        super(errorCode);
    }
}
