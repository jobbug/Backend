package com.example.jobbug.domain.chat.service;

import com.example.jobbug.domain.chat.converter.ChatRoomConverter;
import com.example.jobbug.domain.chat.dto.request.CreateRoomRequest;
import com.example.jobbug.domain.chat.dto.response.GetChatRoomListResponse;
import com.example.jobbug.domain.chat.dto.response.GetChatRoomResponse;
import com.example.jobbug.domain.chat.dto.response.MessageResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.enums.ChatRoomUserRole;
import com.example.jobbug.domain.chat.repository.ChatRoomQueryRepository;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.repository.PostRepository;
import com.example.jobbug.domain.reservation.entity.ChatRoomStatus;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.DuplicateException;
import com.example.jobbug.global.exception.model.ForbiddenException;
import com.example.jobbug.global.exception.model.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomQueryRepository chatRoomQueryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public List<GetChatRoomListResponse> loadAll(Long userId) {
        return chatRoomQueryRepository.findAllByUserIdInAuthorIdOrParticipantId(userId)
                .stream()
                .map(ChatRoomConverter::mapToListResponse)
                .toList();
    }

    @Transactional
    public GetChatRoomResponse load(Long userId, Long roomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
        );

        if(!chatRoom.getAuthor().getId().equals(userId) && !chatRoom.getParticipant().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.NOT_PARTICIPANT_EXCEPTION);
        }

        ChatRoomUserRole role = chatRoom.getAuthor().getId().equals(userId) ? ChatRoomUserRole.AUTHOR : ChatRoomUserRole.PARTICIPANT;

        List<MessageResponse> messages = messageService.loadMessages(roomId);
        return ChatRoomConverter.mapToResponse(chatRoom, role, messages);
    }

    // 수락자가 채팅방 생성 요청
    @Transactional
    public GetChatRoomListResponse createRoom(CreateRoomRequest request, Long userId) {

        if(chatRoomQueryRepository.existsByUserIdInAuthorIdOrParticipantIdAndPostId(userId, request.getPostId())) {
            throw new DuplicateException(ErrorCode.ALREADY_ROOM_EXIST);
        }

        User participant = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION)
        );

        Post post = postRepository.findById(request.getPostId()).orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND_POST_EXCEPTION)
        );

        if(post.getAuthor().getId().equals(userId)) {
            throw new BadRequestException(ErrorCode.FAILED_ROOM_CREATE);
        }

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .author(post.getAuthor())
                .participant(participant)
                .postId(request.getPostId())
                .status(ChatRoomStatus.DO)
                .build());

        return ChatRoomConverter.mapToListResponse(chatRoom);
    }


}
