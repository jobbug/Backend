package com.example.jobbug.domain.main.controller;

import com.example.jobbug.domain.main.service.MainService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.enums.SuccessCode;
import com.example.jobbug.global.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/main/summary")
    public ResponseEntity<?> getSummary(
            @UserId Long userId
    ) {
        try {
            return SuccessResponse.success(SuccessCode.GET_MAIN_SUMMARY_SUCCESS, mainService.getSummary(userId));
        } catch (NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }
}
