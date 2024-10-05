package com.example.jobbug.domain.chat.service;

import com.example.jobbug.domain.chat.converter.MessageConverter;
import com.example.jobbug.domain.chat.dto.request.CreateChatRequest;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.entity.firebase.MessageType;
import com.example.jobbug.domain.chat.exception.FirebaseException;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.chat.repository.MessageRepository;
import com.example.jobbug.domain.chat.util.MessageIdGenerator;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.ForbiddenException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private final MessageIdGenerator messageIdGenerator;

    public void createMessage(CreateChatRequest request, Long userId) {

        // 채팅방 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
        );

        // 사용자 정보 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION)
        );

        // 채팅방 참여자인지 확인
        boolean isAuthor = chatRoom.getAuthor().getId().equals(userId);
        boolean isParticipant = chatRoom.getParticipant().getId().equals(userId);
        if (!isAuthor && !isParticipant) {
            throw new ForbiddenException(ErrorCode.NOT_PARTICIPANT_EXCEPTION);
        }

        long number = messageIdGenerator.nextId();
        
        Message message = Message.builder()
                .number(number)
                .chatRoom(chatRoom)
                .content(request.getContent())
                .senderId(userId)
                .isRead(false)
                .type(MessageType.MESSAGE)
                .build();

        DatabaseReference roomMessageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        ApiFuture<Void> future = roomMessageRef.push().setValueAsync(
                MessageConverter.mapToFirebase(user, message, chatRoom.getId(), MessageType.MESSAGE)
        );

        messageRepository.save(message);

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirebaseException(ErrorCode.FAILED_TO_SEND_MESSAGE);
        }
    }
}
