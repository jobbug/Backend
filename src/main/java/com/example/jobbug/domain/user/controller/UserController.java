package com.example.jobbug.domain.user.controller;

import com.example.jobbug.domain.user.dto.request.UserRegisterRequest;
import com.example.jobbug.domain.user.service.UserService;
import com.example.jobbug.global.dto.ApiResponse;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.model.TokenException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.jobbug.global.exception.enums.SuccessCode.LOGOUT_SUCCESS;
import static com.example.jobbug.global.exception.enums.SuccessCode.REGISTER_SUCCESS;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerUser(
            @RequestHeader String registerToken,
            @RequestPart(value = "image") MultipartFile profileImage,
            @RequestPart(value = "userInfo") UserRegisterRequest request) {
        try {
            return SuccessResponse.success(REGISTER_SUCCESS, userService.registerUser(registerToken, profileImage, request));
        } catch (TokenException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response) {

        userService.logout(response);

        return SuccessNonDataResponse.success(LOGOUT_SUCCESS);
    }

    @GetMapping("/test")
    public String test(
            @AuthenticationPrincipal String userId
    ) {
        return "test " + Long.parseLong(userId);
    }
}

