package com.example.jobbug.domain.main.converter;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.main.dto.response.MainSummaryResponse;
import com.example.jobbug.domain.review.entity.Review;

import java.util.List;

public class MainConverter {
    public static MainSummaryResponse toMainSummaryResponse(List<ChatRoom> notReviewedChatRooms, List<Review> receivedReview, int doingCount) {
        return MainSummaryResponse.builder()
                .fastReviews(MainSummaryResponse.FastReviews.builder()
                        .count(notReviewedChatRooms.size())
                        .reviews(notReviewedChatRooms.stream()
                                .map(chatRoom -> MainSummaryResponse.FastReviews.SimpleReviews.builder()
                                        .chatRoomId(chatRoom.getId())
                                        .nickname(chatRoom.getParticipant().getNickname())
                                        .build())
                                .toList())
                        .build())
                .reviewForMe(MainSummaryResponse.ReviewForMe.builder()
                        .count(receivedReview.size())
                        .reviews(receivedReview.stream()
                                .map(review -> MainSummaryResponse.ReviewForMe.SimpleReviews.builder()
                                        .reviewId(review.getId())
                                        .nickname(review.getWriter().getNickname())
                                        .build())
                                .toList())
                        .build())
                .doing(MainSummaryResponse.Doing.builder()
                        .count(doingCount)
                        .build())
                .build();
    }
}
