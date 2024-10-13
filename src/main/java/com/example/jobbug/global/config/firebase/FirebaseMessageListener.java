package com.example.jobbug.global.config.firebase;


import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.global.config.web.socket.MessageWebSocketHandler;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.NotFoundException;
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

    public void init() {
        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        messageRef.addChildEventListener(new OnChildAddedListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                log.info("Message received: {}", dataSnapshot);
                FirebaseMessage message = dataSnapshot.getValue(FirebaseMessage.class);
                log.info("Message: {}", message);
                ChatRoom chatRoom = chatRoomRepository.findById(message.getRoomId()).orElseThrow(
                        () -> new NotFoundException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
                );

                messageWebSocketHandler.sendMessage(chatRoom.getAuthor().getId(), message);
                messageWebSocketHandler.sendMessage(chatRoom.getParticipant().getId(), message);

            }
        });
    }
}