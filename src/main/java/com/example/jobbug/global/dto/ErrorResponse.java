package com.example.jobbug.global.dto;

import com.example.jobbug.global.exception.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse extends ApiResponse{
    private final int code;
    private final String errorCode;
    private final String message;

    public static ErrorResponse error(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getErrorCode(), errorCode.getMessage());
    }
}
