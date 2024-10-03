package com.example.jobbug.domain.chat.controller;

import com.example.jobbug.domain.chat.dto.request.CreateRoomRequest;
import com.example.jobbug.domain.chat.service.ChatRoomService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.ListWrapperResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<?> createRoom(
            @UserId Long userId,
            @RequestBody CreateRoomRequest request
    ) {
        // 사용자 아이디 출력
        return SuccessResponse.success(
                SuccessCode.CREATE_SUCCESS,
                chatRoomService.createRoom(request, userId),
                "채팅방 생성 성공"
        );
    }

    @GetMapping
    public ResponseEntity<?> loadAll(
            @UserId Long userId
    ) {
        return SuccessResponse.success(
                SuccessCode.GET_SUCCESS,
                new ListWrapperResponse(chatRoomService.findAllByUserId(userId)),
                "모든 채팅방 조회 성공"
        );
        
    }
}
