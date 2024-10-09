package com.example.jobbug.domain.chat.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetChatRoomListResponse {

    private Long roomId;
    private MessageResponse lastMessage;
}
