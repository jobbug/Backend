package com.example.jobbug.domain.reservation.dto.request;

import com.example.jobbug.domain.chat.entity.ChatRoom;
import com.example.jobbug.domain.post.entity.Post;
import com.example.jobbug.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ToString
@Getter
public class ReservationRequest {

    private Long roomId;
    private String address;
    private String addressDetail;
    private String startTime;
    private String endTime;

    public Reservation toEntity(ChatRoom chatRoom) {
        return Reservation.builder()
                .id(null)
                .chatRoom(chatRoom)
                .post(chatRoom.getPost())
                .address(address)
                .addressDetail(addressDetail)
                .startTime(LocalDateTime.of(LocalDate.now(), LocalTime.parse(startTime)))
                .endTime(LocalDateTime.of(LocalDate.now(), LocalTime.parse(endTime)))
                .build();
    }

}
