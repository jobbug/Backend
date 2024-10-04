package com.example.jobbug.domain.review.controller;

import com.example.jobbug.domain.review.dto.request.SaveReviewRequest;
import com.example.jobbug.domain.review.service.ReviewService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> saveReview(
            @UserId Long userId,
            @RequestBody SaveReviewRequest request
    ) {
        try {
            reviewService.saveReview(userId, request);
            return SuccessNonDataResponse.success(SuccessCode.SAVE_REVIEW_SUCCESS);
        } catch (NotFoundException | BadRequestException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }
}
