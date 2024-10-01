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
public class SuccessNonDataResponse extends ApiResponse{
    private final int code;
    private final String message;

    public static ResponseEntity<?> success(SuccessCode successCode) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new SuccessNonDataResponse(successCode.getHttpStatus().value(), successCode.getMessage()));
    }

    public static ResponseEntity<?> success(SuccessCode successCode, String message) {
        return ResponseEntity.status(successCode.getHttpStatus()).body(new SuccessNonDataResponse(successCode.getHttpStatus().value(), message));
    }
}
