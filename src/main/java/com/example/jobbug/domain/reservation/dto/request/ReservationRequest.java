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
        // 만약 현재 시간보다 이전일 경우 다음 날짜로 설정
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.parse(startTime));
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.parse(endTime));
        if (LocalTime.parse(startTime).isBefore(LocalTime.now())) {
            start = start.plusDays(1);
            end = end.plusDays(1);
        }

        return Reservation.builder()
                .id(null)
                .chatRoom(chatRoom)
                .post(chatRoom.getPost())
                .address(address)
                .addressDetail(addressDetail)
                .startTime(start)
                .endTime(end)
                .build();
    }

}
