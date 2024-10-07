package com.example.jobbug.domain.reservation.dto.response;

import com.example.jobbug.domain.reservation.entity.Reservation;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateReservationResponse {
    private Long reservationId;

    public static CreateReservationResponse fromEntity(Reservation reservation) {
        return CreateReservationResponse.builder()
                .reservationId(reservation.getId())
                .build();
    }
}
