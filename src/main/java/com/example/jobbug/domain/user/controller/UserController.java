package com.example.jobbug.domain.user.controller;

import com.example.jobbug.domain.user.dto.request.UpdateUserRequest;
import com.example.jobbug.domain.user.dto.request.UserRegisterRequest;
import com.example.jobbug.domain.user.service.UserService;
import com.example.jobbug.global.config.web.UserId;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.dto.SuccessNonDataResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.example.jobbug.global.exception.model.TokenException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.jobbug.global.exception.enums.SuccessCode.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerUser(
            @RequestHeader String registerToken,
            @RequestPart(value = "image", required = false) MultipartFile profileImage,
            @RequestPart(value = "userInfo") UserRegisterRequest request) {
        try {
            return SuccessResponse.success(REGISTER_SUCCESS, userService.registerUser(registerToken, profileImage, request));
        } catch (TokenException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(
            @UserId Long userId,
            @ModelAttribute UpdateUserRequest request
    ) {
        userService.updateUser(userId, request);
        return SuccessNonDataResponse.success(UPDATE_USER_SUCCESS);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response) {

        userService.logout(response);

        return SuccessNonDataResponse.success(LOGOUT_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(
            @UserId Long userId
    ) {
        try {
            return SuccessResponse.success(GET_USER_INFO_SUCCESS, userService.getUserInfo(userId));
        } catch (NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

    @GetMapping("/duplicate")
    public ResponseEntity<?> checkDuplicate(
            @RequestParam String nickname
    ) {
        userService.checkDuplicate(nickname);
        return SuccessNonDataResponse.success(CHECK_DUPLICATE_NICKNAME_SUCCESS);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Long userId
    ) {
        try {
            return SuccessResponse.success(GET_USER_PROFILE_SUCCESS, userService.getUserProfile(userId));
        } catch (NotFoundException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }

}

