package com.example.jobbug.domain.reservation.controller;

import com.example.jobbug.domain.reservation.dto.request.ReservationRequest;
import com.example.jobbug.domain.reservation.service.ReservationService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@UserId Long userId, @RequestBody ReservationRequest request) {
        return SuccessResponse.success(
                SuccessCode.CREATE_RESERVATION_SUCCESS,
                reservationService.createReservation(userId, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@UserId Long userId, @PathVariable Long id) {
        return SuccessResponse.success(
                SuccessCode.GET_RESERVATION_SUCCESS,
                reservationService.getReservation(userId, id)
        );
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptReservation(@UserId Long userId, @PathVariable Long id) {
        reservationService.acceptReservation(userId, id);
        return SuccessNonDataResponse.success(
                SuccessCode.ACCEPT_RESERVATION_SUCCESS
        );
    }
}
