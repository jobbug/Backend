package com.example.jobbug.domain.chat.service;

import com.example.jobbug.domain.chat.converter.ChatRoomConverter;
import com.example.jobbug.domain.chat.dto.request.CreateRoomRequest;
import com.example.jobbug.domain.chat.dto.response.ChatRoomResponse;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.repository.ChatRoomQueryRepository;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
//    private final ChatRoomQueryRepository chatRoomQueryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

//    public List<ChatRoomResponse> findAllByUserIdInAuthorIdOrParticipantId(Long userId) {
//        return chatRoomQueryRepository.findAllByUserIdInAuthorIdOrParticipantId(userId)
//                .stream()
//                .map(ChatRoomConverter::mapToResponse)
//                .toList();
//    }

    // 수락자가 채팅방 생성 요청
    public ChatRoomResponse createRoom(CreateRoomRequest request, Long userId) {
        // TODO-HONG : 이미 참여중인 채팅방인지 확인
        User participant = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("해당 사용자 정보를 찾을 수 없습니다. (ID: %d)", userId))
        );

        // TODO-HONG : POST 정보 불러와서 작성자 정보 가져와야 함 (요청자 기본 아이디 0)
        User author = userRepository.findById(0L).orElseThrow(
                () -> new NotFoundException(String.format("해당 사용자 정보를 찾을 수 없습니다. (ID: %d)", userId))
        );

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .author(author)
                .participant(participant)
                .postId(0L) // TODO-HONG : 게시글 아이디 수정
                .status("DO") // TODO-HONG : 구현된 것 보고 Enum 변경
                .build());

        return ChatRoomConverter.mapToResponse(chatRoom);
    }
}
