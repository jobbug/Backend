package com.example.jobbug.domain.firebase.service;

import com.example.jobbug.domain.chat.converter.MessageConverter;
import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.chat.enums.MessageType;
import com.example.jobbug.domain.chat.exception.FirebaseException;
import com.example.jobbug.domain.firebase.entity.FirebaseMessageData;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {
    public void sendFirebaseMessage(Message message, FirebaseMessageData data) {
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
}
