package com.example.jobbug.global.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    /**
     * 예시 코드, 아래에 원하는 형식으로 추가하면 됩니다.
     */
    REGISTER_SUCCESS(HttpStatus.OK, "회원가입 성공입니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공입니다."),
    GET_USER_INFO_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공입니다."),;


    private final HttpStatus httpStatus;
    private final String message;
}