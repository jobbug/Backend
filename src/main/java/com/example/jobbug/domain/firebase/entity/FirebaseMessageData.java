package com.example.jobbug.domain.firebase.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseMessageData {
    private Long reservationId;
    private String phone;
}
