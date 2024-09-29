package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class TokenException extends JobbugException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

}
