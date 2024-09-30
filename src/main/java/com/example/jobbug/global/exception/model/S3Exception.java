package com.example.jobbug.global.exception.model;

import com.example.jobbug.global.exception.enums.ErrorCode;

public class S3Exception extends JobbugException {
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }
}
