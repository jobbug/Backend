package com.example.jobbug.global.dto;

import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> extends ApiResponse{
    private final int code;
    private final String message;
    private final T data;

    public static <T> ResponseEntity<?> success(SuccessCode successCode, T data) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new SuccessResponse(successCode.getHttpStatus().value(), successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<?> success(SuccessCode successCode, T data, String message) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new SuccessResponse(successCode.getHttpStatus().value(), message, data));
    }
}
