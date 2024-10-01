package com.example.jobbug.domain.chat.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    private Long roomId;
    private MessageResponse lastMessage;
}
