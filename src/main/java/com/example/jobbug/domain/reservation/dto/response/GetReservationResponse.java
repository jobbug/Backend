package com.example.jobbug.domain.reservation.dto.response;

import com.example.jobbug.domain.reservation.entity.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class GetReservationResponse {
    private Long reservationId;
    private String address;
    private String addressDetail;
    private String startTime;
    private String endTime;


    public static GetReservationResponse fromEntity(Reservation reservation) {
        return GetReservationResponse.builder()
                .reservationId(reservation.getId())
                .address(reservation.getAddress())
                .addressDetail(reservation.getAddressDetail())
                .startTime(reservation.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .endTime(reservation.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .build();
    }
}
