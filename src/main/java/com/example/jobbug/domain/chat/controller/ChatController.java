package com.example.jobbug.domain.chat.controller;

import com.example.jobbug.domain.chat.dto.request.CreateChatRequest;
import com.example.jobbug.domain.chat.service.MessageService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<?> chat(
            @UserId Long userId,
            @RequestBody CreateChatRequest request
    ) {
        messageService.createMessage(request, userId);
        return SuccessNonDataResponse.success(
                SuccessCode.CREATE_SUCCESS,
                "채팅 전송 성공"
        );
    }
}
