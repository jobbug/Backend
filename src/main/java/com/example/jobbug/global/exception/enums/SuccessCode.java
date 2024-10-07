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
    // 200
    REGISTER_SUCCESS(HttpStatus.OK, "회원가입 성공입니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공입니다."),
    GET_USER_INFO_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공입니다."),
    CHECK_DUPLICATE_NICKNAME_SUCCESS(HttpStatus.OK, "사용 가능한 닉네임"),
    GET_MAIN_POST_SUCCESS(HttpStatus.OK, "메인 잡아주세요 조회 성공입니다."),
    GET_POST_DETAIL_SUCCESS(HttpStatus.OK, "게시글 상세 조회 성공입니다."),
    GET_REVIEW_SUCCESS(HttpStatus.OK, "후기 상세 조회 성공입니다."),
    GET_MAIN_SUMMARY_SUCCESS(HttpStatus.OK, "메인 요약 조회 성공입니다."),
    GET_ROOM_SUCCESS(HttpStatus.OK, "채팅방 조회 성공입니다."),
    GET_RESERVATION_SUCCESS(HttpStatus.OK, "예약 조회 성공입니다."),
    ACCEPT_RESERVATION_SUCCESS(HttpStatus.OK, "예약 수락 성공입니다."),
    CANCEL_RESERVATION_SUCCESS(HttpStatus.OK, "예약 취소 성공입니다."),

    // 201
    CREATE_ROOM_SUCCESS(HttpStatus.CREATED, "채팅방 생성 성공입니다."),
    CREATE_CHAT_SUCCESS(HttpStatus.CREATED, "채팅 전송 성공입니다."),
    CREATE_RESERVATION_SUCCESS(HttpStatus.CREATED, "예약 성공입니다."),
    CREATE_POST_SUCCESS(HttpStatus.CREATED, "게시글 작성 성공입니다."),
    UPLOAD_AI_IMAGE_SUCCESS(HttpStatus.CREATED, "AI 이미지 업로드 성공입니다."),
    UPLOAD_EDITED_IMAGE_SUCCESS(HttpStatus.CREATED, "편집 이미지 업로드 성공입니다."),
    SAVE_REVIEW_SUCCESS(HttpStatus.CREATED, "후기 작성 성공입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}