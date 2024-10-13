package com.example.jobbug.domain.chat.dto.response;

import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class GetChatResponse {
    private String type;
    private List<FirebaseMessage> messages;
}
