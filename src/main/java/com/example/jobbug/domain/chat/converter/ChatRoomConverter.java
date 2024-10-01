package com.example.jobbug.domain.chat.converter;

import com.example.jobbug.domain.chat.dto.response.ChatRoomResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.global.domain.BaseEntity;

import java.util.Comparator;

public class ChatRoomConverter {

    public static ChatRoomResponse mapToResponse(ChatRoom chatRoom) {
        Message lastMessage = chatRoom.getMessages()
                .stream()
                .max(Comparator.comparing(BaseEntity::getCreatedAt))
                .orElse(null);
        return new ChatRoomResponse(chatRoom.getId(), MessageConverter.mapToResponse(lastMessage));
    }
}
