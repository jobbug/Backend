package com.example.jobbug.domain.review.service;

import com.example.jobbug.domain.badge.entity.Badge;
import com.example.jobbug.domain.badge.enums.BadgeType;
import com.example.jobbug.domain.badge.repository.BadgeRepository;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.repository.PostRepository;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final BadgeRepository badgeRepository;

    @Transactional
    public void saveReview(Long userId, SaveReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION));
        Post post = postRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST_EXCEPTION));

        if (!chatRoom.getAuthor().equals(user)) {
            throw new BadRequestException(ErrorCode.NOT_CHATROOM_WRITER);
        }

        Review review = Review.of(user, chatRoom, post, request);
        reviewRepository.save(review);
        post.finish();
        postRepository.save(post);

        if (reviewRepository.countByAuthor(user) == 10) {
            Badge badge = Badge.builder()
                    .user(user)
                    .name(BadgeType.EXPERT.getName())
                    .type(BadgeType.EXPERT)
                    .build();
            badgeRepository.save(badge);
        }

        List<Review> reviewList = reviewRepository.findByAuthor(chatRoom.getParticipant());
        String currentBugType = post.getBugType();
        int count = 0;
        for(Review r : reviewList) {
            Post p = r.getPost();
            if(p.getBugType().equals(currentBugType)) {
                count++;
            }
        }
        if (count == 10) {
            Badge badge = Badge.builder()
                    .user(chatRoom.getParticipant())
                    .name(String.format(currentBugType + " " + BadgeType.KILLER.getName()))
                    .type(BadgeType.KILLER)
                    .build();
            badgeRepository.save(badge);
        }
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
