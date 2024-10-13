package com.example.jobbug.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadChatRoomRequest {
    private Long roomId;
    private Long number;
}
