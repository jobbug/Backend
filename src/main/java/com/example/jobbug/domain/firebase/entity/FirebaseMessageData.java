package com.example.jobbug.domain.firebase.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseMessageData {
    private Long reservationId;
    private String phone;
}
