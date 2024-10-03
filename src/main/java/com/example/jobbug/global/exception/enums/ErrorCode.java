package com.example.jobbug.global.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 예시 코드, 아래에 원하는 형식으로 추가하면 됩니다.
     */

    // 400
    INVALID_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "E101", "유효하지 않은 토큰을 입력했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E001", "적절하지 않은 요청 값입니다."),
    AI_IMAGE_CREATE_EXCEPTION(HttpStatus.BAD_REQUEST, "E302", "AI 이미지 생성에 실패했습니다."),
    FAILED_ROOM_CREATE(HttpStatus.BAD_REQUEST, "E041", "채팅방 생성 실패"),

    // 401
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E401", "인증 정보가 유효하지 않습니다."),

    // 403
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "E403", "접근 권한이 없습니다."),

    //404
    NOT_FOUND_RESOURCE_EXCEPTION(HttpStatus.NOT_FOUND, "E404", "해당 자원을 찾을 수 없습니다."),
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "E000", "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_ADDRESS_EXCEPTION(HttpStatus.NOT_FOUND, "E301", "해당 주소를 찾을 수 없습니다."),

    // 405 METHOD_NOT_ALLOWED
    METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E405", "지원하지 않는 메소드 입니다."),

    // 409
    ALREADY_ROOM_EXIST(HttpStatus.BAD_REQUEST, "E042", "이미 존재하는 채팅방입니다."),

    // 500
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 내부 오류입니다."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E501", "S3 파일 업로드에 실패했습니다."),;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}