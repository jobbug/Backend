package com.example.jobbug.domain.review.dto.request;

import lombok.Data;

@Data
public class SaveReviewRequest {
    private Long chatRoomId;
    private boolean isArrive;
    private int lateTime;
    private char isSuccess;
    private String content;
    private int point;
}
