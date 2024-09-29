package com.example.jobbug.global.dto;

import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessNonDataResponse extends ApiResponse{
    private final int code;
    private final String message;

    public static SuccessNonDataResponse success(SuccessCode successCode) {
        return new SuccessNonDataResponse(successCode.getHttpStatus().value(), successCode.getMessage());
    }
}
