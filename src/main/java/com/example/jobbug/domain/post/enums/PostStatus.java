package com.example.jobbug.domain.post.enums;

public enum PostStatus {
    DO("진행중"),
    DONE("완료"),
    CANCEL("취소");

    private final String status;
    PostStatus(String status) {
        this.status = status;
    }
}
