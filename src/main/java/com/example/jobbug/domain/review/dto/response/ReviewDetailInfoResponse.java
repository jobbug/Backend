package com.example.jobbug.domain.review.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDetailInfoResponse {
    private Long reviewId;
    private boolean isArrive;
    private int lateTime;
    private char isSuccess;
    private String content;
    private int point;
}
