package com.example.jobbug.domain.chat.service;

import com.example.jobbug.domain.chat.converter.MessageConverter;
import com.example.jobbug.domain.chat.dto.request.CreateChatRequest;
import com.example.jobbug.domain.chat.dto.response.MessageResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.chat.exception.FirebaseException;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.chat.repository.MessageRepository;
import com.example.jobbug.domain.chat.util.MessageIdGenerator;
import com.example.jobbug.domain.firebase.service.FirebaseService;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.ForbiddenException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final FirebaseService firebaseService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private final MessageIdGenerator messageIdGenerator;

    @Transactional
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
                .sender(user)
                .isRead(false)
                .type(MessageType.MESSAGE)
                .build();
        firebaseService.sendFirebaseMessage(message, null);
        messageRepository.save(message);
    }

    public List<MessageResponse> loadMessages(Long roomId) {

        // TODO-HONG : 입력 받기
        int page = 0;
        int size = 30;

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
        );

        return messageRepository.findAllByChatRoomIdOrderByCreatedAtDesc(
                chatRoom.getId(), PageRequest.of(page, size)
        ).map(MessageConverter::mapToResponse).getContent();
    }
}
