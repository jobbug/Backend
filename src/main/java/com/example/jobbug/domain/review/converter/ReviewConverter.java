package com.example.jobbug.domain.review.converter;

import com.example.jobbug.domain.review.dto.response.ReviewDetailInfoResponse;
import com.example.jobbug.domain.review.entity.Review;

public class ReviewConverter {
    public static ReviewDetailInfoResponse toReviewDetailInfoResponse(Review review) {
        return ReviewDetailInfoResponse.builder()
                .reviewId(review.getId())
                .isArrive(review.isArrive())
                .lateTime(review.getLateTime())
                .isSuccess(review.getIsSuccess())
                .content(review.getContent())
                .point(review.getPoint())
                .build();
    }
}
