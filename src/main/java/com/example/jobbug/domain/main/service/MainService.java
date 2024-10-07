package com.example.jobbug.domain.main.service;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.main.converter.MainConverter;
import com.example.jobbug.domain.main.dto.response.MainSummaryResponse;
import com.example.jobbug.domain.post.enums.PostStatus;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import com.example.jobbug.domain.review.entity.Review;
import com.example.jobbug.domain.review.repository.ReviewRepository;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public MainSummaryResponse getSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION));

        List<ChatRoom> notReviewedChatRooms = getNotReviewedChatRooms(user);
        List<Review> receivedReviews = reviewRepository.findByAuthor(user);
        int ongoingPostCount = getOngoingPostCount(user);

        return MainConverter.toMainSummaryResponse(notReviewedChatRooms, receivedReviews, ongoingPostCount);
    }

    private List<ChatRoom> getNotReviewedChatRooms(User user) {
        return user.getCreatedChatRooms().stream()
                .filter(chatRoom -> ChatRoomStatus.MATCHED.equals(chatRoom.getStatus()))  // 예약 완료된 chatRoom
                .filter(chatRoom -> chatRoom.getReservation().getEndTime().isBefore(LocalDateTime.now()))  // EndTime이 지난 chatRoom
                .filter(chatRoom -> reviewRepository.findByRoomId(chatRoom).isEmpty())  // Review가 없는 chatRoom
                .toList();
    }

    private int getOngoingPostCount(User user) {
        return (int) user.getPosts().stream()
                .filter(post -> PostStatus.DO.equals(post.getStatus()))
                .count();
    }
}

