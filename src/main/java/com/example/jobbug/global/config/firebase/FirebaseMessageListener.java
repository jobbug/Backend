package com.example.jobbug.global.config.firebase;


import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.firebase.FirebaseMessage;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.global.config.web.MessageWebSocketHandler;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseMessageListener {

    private final MessageWebSocketHandler messageWebSocketHandler;
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void init() {
        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        messageRef.addChildEventListener(new OnChildAddedListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                FirebaseMessage message = dataSnapshot.getValue(FirebaseMessage.class);
                ChatRoom chatRoom = chatRoomRepository.findById(message.getRoomId()).orElseThrow(
                        () -> new NotFoundException(String.format("채팅방 정보를 찾을 수 없습니다. (ID: %d)", message.getRoomId()))
                );

                messageWebSocketHandler.sendMessage(chatRoom.getAuthor().getId(), message);
                messageWebSocketHandler.sendMessage(chatRoom.getParticipant().getId(), message);
            }
        });
    }
}