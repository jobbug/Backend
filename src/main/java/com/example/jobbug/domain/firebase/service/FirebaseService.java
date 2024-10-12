package com.example.jobbug.domain.firebase.service;

import com.example.jobbug.domain.chat.converter.MessageConverter;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.chat.exception.FirebaseException;
import com.example.jobbug.domain.chat.repository.ChatRoomRepository;
import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import com.example.jobbug.domain.firebase.entity.FirebaseMessageData;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.google.firebase.database.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final ChatRoomRepository chatRoomRepository;

    public void sendFirebaseMessage(Message message, FirebaseMessageData data) {
        log.info("Send message to firebase: {}", message);
        DatabaseReference roomMessageRef = FirebaseDatabase.getInstance()
                .getReference("messages");
        try {
            roomMessageRef.push().setValueAsync(
                    MessageConverter.mapToFirebase(message, data)
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirebaseException(ErrorCode.FAILED_TO_SEND_MESSAGE);
        }
    }


    public List<FirebaseMessage> getMessagesByRoomId(Long roomId) {
        DatabaseReference roomMessageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        Query query = roomMessageRef.orderByChild("roomId").equalTo(roomId);

        CompletableFuture<List<FirebaseMessage>> futureMessages = new CompletableFuture<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseMessage> messages = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseMessage message = snapshot.getValue(FirebaseMessage.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }

                futureMessages.complete(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 예외 발생 시 CompletableFuture에 오류 완료
                futureMessages.completeExceptionally(databaseError.toException());
            }
        });

        try {
            // CompletableFuture가 완료될 때까지 대기
            return futureMessages.get();
        } catch (InterruptedException | ExecutionException e) {
            // 예외 발생 시 커스텀 예외 던지기
            throw new FirebaseException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public FirebaseMessage getLastMessageByRoomId(Long roomId) {
        DatabaseReference roomMessageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        Query query = roomMessageRef.orderByChild("roomId").equalTo(roomId).limitToLast(1);

        CompletableFuture<FirebaseMessage> futureMessage = new CompletableFuture<>();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseMessage message = snapshot.getValue(FirebaseMessage.class);
                    if (message != null && message.getType() == MessageType.MESSAGE) {
                        futureMessage.complete(message);
                        return;
                    }
                }
                futureMessage.complete(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                futureMessage.completeExceptionally(databaseError.toException());
            }
        });

        try {
            return futureMessage.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirebaseException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    public void readChatRoom(Long roomId, Long number, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new FirebaseException(ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION)
        );

        if(chatRoom.getAuthor().getId().equals(userId)) {
            return;
        }

        DatabaseReference roomMessageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        Query query = roomMessageRef.orderByChild("roomId").equalTo(roomId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseMessage message = snapshot.getValue(FirebaseMessage.class);
                    if (message != null && message.getNumber().equals(number)) {
                        if(message.getSenderId().equals(userId)) {
                            return;
                        } else {
                            message.setRead(true);
                            snapshot.getRef().setValueAsync(message);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new FirebaseException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        });
    }
}
