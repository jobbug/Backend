package com.example.jobbug.global.config.firebase;


import com.example.jobbug.domain.chat.entity.firebase.FirebaseMessage;
import com.google.firebase.database.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class FirebaseMessageListener {

    public void init() {
        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                .getReference("messages");

        messageRef.addChildEventListener(new OnChildAddedListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println(dataSnapshot.getValue());
                // TODO : 새롭게 추가된 데이터만 알림 전송
            }
        });
    }
}