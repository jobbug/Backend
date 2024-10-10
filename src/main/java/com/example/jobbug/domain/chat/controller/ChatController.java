package com.example.jobbug.domain.chat.controller;

import com.example.jobbug.domain.chat.dto.request.CreateChatRequest;
import com.example.jobbug.domain.chat.service.MessageService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
                SuccessCode.CREATE_CHAT_SUCCESS
        );
    }
}
