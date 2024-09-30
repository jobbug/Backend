package com.example.jobbug.global.dto;

import com.example.jobbug.global.exception.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse extends ApiResponse {
    private final int code;
    private final String errorCode;
    private final String message;

    public static ResponseEntity<?> error(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getErrorCode(), errorCode.getMessage()));
    }

    public static ResponseEntity<?> error(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getErrorCode(), message));
    }
}
