package com.example.jobbug.domain.reservation.service;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.chat.util.MessageIdGenerator;
import com.example.jobbug.domain.firebase.entity.FirebaseMessageData;
import com.example.jobbug.domain.firebase.service.FirebaseService;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.repository.PostRepository;
import com.example.jobbug.domain.reservation.dto.request.ReservationRequest;
import com.example.jobbug.domain.reservation.dto.response.CreateReservationResponse;
import com.example.jobbug.domain.reservation.dto.response.GetReservationResponse;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import com.example.jobbug.domain.reservation.entity.Reservation;
import com.example.jobbug.domain.reservation.repository.ReservationRepository;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.JobbugException;
import com.example.jobbug.global.exception.model.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReservationRepository reservationRepository;
    private final PostRepository postRepository;

    private final FirebaseService firebaseService;

    private final MessageIdGenerator messageIdGenerator;

    @Transactional
    public CreateReservationResponse createReservation(Long userId, ReservationRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
        );

        Post post = postRepository.findById(chatRoom.getPostId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_POST_EXCEPTION)
        );

        if (!chatRoom.getAuthor().getId().equals(userId)) {
            throw new JobbugException(ErrorCode.RESERVATION_CREATION_NOT_ALLOWED_EXCEPTION);
        }

        if (chatRoom.getReservation() != null) {
            throw new JobbugException(ErrorCode.DUPLICATED_RESERVATION_EXCEPTION);
        }

        Reservation reservation = reservationRepository.save(
                request.toEntity(chatRoom, post)
        );

        long number = messageIdGenerator.nextId();

        Message message = Message.builder()
                .number(number)
                .chatRoom(chatRoom)
                .content("새로운 예약 요청")
                .sender(null)
                .isRead(false)
                .type(MessageType.RESERVATION)
                .build();

        firebaseService.sendFirebaseMessage(
                message, FirebaseMessageData.builder()
                        .reservationId(reservation.getId())
                        .build()
        );

        return CreateReservationResponse.fromEntity(
                reservation
        );
    }

    @Transactional
    public GetReservationResponse getReservation(Long userId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION_EXCEPTION)
        );

        ChatRoom room = reservation.getChatRoom();

        // 해당 사용자가 작성자나 참여자일 경우만 조회 가능
        if (!(room.getAuthor().getId().equals(userId) || room.getParticipant().getId().equals(userId))) {
            throw new JobbugException(ErrorCode.RESERVATION_ACCESS_NOT_ALLOWED_EXCEPTION);
        }

        return GetReservationResponse.fromEntity(reservation);
    }

    @Transactional
    public void acceptReservation(Long userId, Long reservationId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION)
        );

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION_EXCEPTION)
        );

        ChatRoom chatRoom = reservation.getChatRoom();

        if (!chatRoom.getParticipant().getId().equals(userId)) {
            throw new JobbugException(ErrorCode.RESERVATION_ACCEPT_NOT_ALLOWED_EXCEPTION);
        }

        if(chatRoom.getStatus() == ChatRoomStatus.MATCHED) {
            throw new JobbugException(ErrorCode.RESERVATION_ALREADY_MATCHED_EXCEPTION);
        }

        chatRoom.matchReservation();

        long number = messageIdGenerator.nextId();

        Message message = Message.builder()
                .number(number)
                .chatRoom(chatRoom)
                .content("매칭 성공")
                .sender(null)
                .isRead(false)
                .type(MessageType.MATCHED)
                .build();

        firebaseService.sendFirebaseMessage(
                message, FirebaseMessageData.builder()
                        .reservationId(reservation.getId())
                        .phone(chatRoom.getAuthor().getPhone())
                        .build()
        );
    }
}
