package com.example.jobbug.domain.chat.converter;

import com.example.jobbug.domain.chat.dto.response.GetChatRoomListResponse;
import com.example.jobbug.domain.chat.dto.response.GetChatRoomResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.enums.ChatRoomUserRole;
import com.example.jobbug.global.domain.BaseEntity;

import java.util.Comparator;
import java.util.List;

public class ChatRoomConverter {

    public static GetChatRoomListResponse mapToListResponse(ChatRoom chatRoom) {

        Message lastMessage = chatRoom.getMessages()
                .stream()
                .max(Comparator.comparing(BaseEntity::getCreatedAt))
                .orElse(null);
        return new GetChatRoomListResponse(chatRoom.getId(), MessageConverter.mapToResponse(lastMessage));
    }

    public static GetChatRoomResponse mapToResponse(ChatRoom chatRoom, ChatRoomUserRole role) {
        Long reservationId = null;
        Long reviewId = null;

        if(chatRoom.getReservation() != null) {
            reservationId = chatRoom.getReservation().getId();
        }

        if(chatRoom.getReview() != null) {
            reviewId = chatRoom.getReview().getId();
        }

        return GetChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .authorId(chatRoom.getAuthor().getId())
                .participantId(chatRoom.getParticipant().getId())
                .role(role)
                .reservationId(reservationId)
                .reviewId(reviewId)
                .messages(
                        chatRoom.getMessages()
                                .stream()
                                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                                .limit(30)
                                .map(MessageConverter::mapToResponse)
                                .toList() // TODO-HONG : JPA로 최적화
                )
                .build();
    }
}
