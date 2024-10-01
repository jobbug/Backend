package com.example.jobbug.domain.chat.controller;

import com.example.jobbug.domain.chat.dto.request.CreateRoomRequest;
import com.example.jobbug.domain.chat.service.ChatRoomService;
import com.example.jobbug.global.dto.ListWrapperResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomRequest request) {

        Long userId = 1L;
        return SuccessResponse.success(
                SuccessCode.CREATE_SUCCESS,
                chatRoomService.createRoom(request, userId),
                "채팅방 생성 성공"
        );
    }

    @GetMapping
    public ResponseEntity<?> loadAll() {
        Long userId = 1L; // TODO-HONG: 수락자 기본 아이디 1
        return SuccessResponse.success(
                SuccessCode.GET_SUCCESS,
                new ListWrapperResponse(chatRoomService.findAllByUserId(userId)),
                "모든 채팅방 조회 성공"
        );
        
    }
}
