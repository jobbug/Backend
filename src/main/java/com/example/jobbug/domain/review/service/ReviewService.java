package com.example.jobbug.domain.review.service;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.review.converter.ReviewConverter;
import com.example.jobbug.domain.review.dto.request.SaveReviewRequest;
import com.example.jobbug.domain.review.dto.response.ReviewDetailInfoResponse;
import com.example.jobbug.domain.review.entity.Review;
import com.example.jobbug.domain.review.repository.ReviewRepository;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void saveReview(Long userId, SaveReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION));

        if (!chatRoom.getAuthor().equals(user)) {
            throw new BadRequestException(ErrorCode.NOT_CHATROOM_WRITER);
        }

        Review review = Review.of(user, chatRoom, request);
        reviewRepository.save(review);
    }

    public ReviewDetailInfoResponse getReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_REVIEW_EXCEPTION));

        // 리뷰 작성자 또는 리뷰 대상자인지 확인
        if (!(review.getAuthor().getId().equals(user.getId()) || review.getWriter().getId().equals(user.getId()))) {
            throw new BadRequestException(ErrorCode.NOT_REVIEW_AUTHORIZATION_EXCEPTION);
        }

        return ReviewConverter.toReviewDetailInfoResponse(review);
    }
}