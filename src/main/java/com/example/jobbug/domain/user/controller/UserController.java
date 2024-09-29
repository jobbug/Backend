package com.example.jobbug.domain.user.controller;

import com.example.jobbug.domain.user.dto.request.UserRegisterRequest;
import com.example.jobbug.domain.user.service.UserService;
import com.example.jobbug.global.dto.ApiResponse;
import com.example.jobbug.global.dto.ErrorResponse;
import com.example.jobbug.global.dto.SuccessResponse;
import com.example.jobbug.global.exception.model.TokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.jobbug.global.exception.enums.SuccessCode.REGISTER_SUCCESS;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse registerUser(@RequestHeader String registerToken, @RequestBody UserRegisterRequest request) {
        try {
            return SuccessResponse.success(REGISTER_SUCCESS, userService.registerUser(registerToken, request));
        } catch (TokenException e) {
            return ErrorResponse.error(e.getErrorCode());
        }
    }
}

