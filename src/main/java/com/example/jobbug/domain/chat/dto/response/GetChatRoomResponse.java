package com.example.jobbug.domain.chat.dto.response;

import com.example.jobbug.domain.chat.enums.ChatRoomUserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetChatRoomResponse {
    private Long roomId;
    private Long authorId;
    private Long participantId;
    private ChatRoomUserRole role;
    private Long reservationId;
    private Long reviewId;
}
