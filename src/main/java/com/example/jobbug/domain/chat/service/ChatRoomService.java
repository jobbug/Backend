package com.example.jobbug.domain.chat.service;

import com.example.jobbug.domain.chat.converter.ChatRoomConverter;
import com.example.jobbug.domain.chat.dto.request.CreateRoomRequest;
import com.example.jobbug.domain.chat.dto.response.ChatRoomResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.repository.ChatRoomQueryRepository;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.post.repository.PostRepository;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.BadRequestException;
import com.example.jobbug.global.exception.model.DuplicateException;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public List<ChatRoomResponse> findAllByUserId(Long userId) {
        return chatRoomQueryRepository.findAllByUserIdInAuthorIdOrParticipantId(userId)
                .stream()
                .map(ChatRoomConverter::mapToResponse)
                .toList();
    }

    // 수락자가 채팅방 생성 요청
    public ChatRoomResponse createRoom(CreateRoomRequest request, Long userId) {

        if(chatRoomQueryRepository.existsByUserIdInAuthorIdOrParticipantIdAndPostId(userId, request.postId())) {
            throw new DuplicateException(ErrorCode.ALREADY_ROOM_EXIST, "이미 참여 중인 채팅방입니다.");
        }

        User participant = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("해당 사용자 정보를 찾을 수 없습니다. (ID: %d)", userId))
        );

        Post post = postRepository.findById(request.postId()).orElseThrow(
                () -> new NotFoundException(String.format("게시글 정보를 찾을 수 없습니다. (ID: %d)", request.postId()))
        );

        if(post.getAuthor().getId().equals(userId)) {
            throw new BadRequestException(ErrorCode.FAILED_ROOM_CREATE, "게시글 작성자는 채팅방을 생성할 수 없습니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .author(post.getAuthor())
                .participant(participant)
                .postId(request.postId())
                .status("DO") // TODO-HONG : 구현된 것 보고 Enum 변경
                .build());

        return ChatRoomConverter.mapToResponse(chatRoom);
    }
}
