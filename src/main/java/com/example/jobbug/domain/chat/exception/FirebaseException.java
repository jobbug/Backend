package com.example.jobbug.domain.chat.exception;

import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.JobbugException;

public class FirebaseException extends JobbugException {
    public FirebaseException() {
        super(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }

    public FirebaseException(String message) {
        super(ErrorCode.INTERNAL_SERVER_EXCEPTION, message);
    }
}
