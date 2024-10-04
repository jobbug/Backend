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
    NOT_CHATROOM_WRITER(HttpStatus.BAD_REQUEST, "E204", "해당 채팅방의 후기를 작성할 권한이 없습니다."),
    NOT_REVIEW_AUTHORIZATION_EXCEPTION(HttpStatus.BAD_REQUEST, "E205", "해당 후기에 대한 조회 권한이 없습니다."),

    // 401
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E401", "인증 정보가 유효하지 않습니다."),

    //404
    NOT_FOUND_RESOURCE_EXCEPTION(HttpStatus.NOT_FOUND, "E404", "해당 자원을 찾을 수 없습니다."),
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "E000", "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_ADDRESS_EXCEPTION(HttpStatus.NOT_FOUND, "E301", "해당 주소를 찾을 수 없습니다."),
    NOT_FOUND_POST_EXCEPTION(HttpStatus.NOT_FOUND, "E202", "해당 게시글을 찾을 수 없습니다."),
    NOT_FOUND_CHATROOM_EXCEPTION(HttpStatus.NOT_FOUND, "E204", "해당 채팅방을 찾을 수 없습니다."),
    NOT_FOUND_REVIEW_EXCEPTION(HttpStatus.NOT_FOUND, "E205", "해당 후기를 찾을 수 없습니다."),

    // 405 METHOD_NOT_ALLOWED
    METHOD_NOT_ALLOWED_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "E405", "지원하지 않는 메소드 입니다."),

    // 500
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 내부 오류입니다."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E501", "S3 파일 업로드에 실패했습니다."),;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}