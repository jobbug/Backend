package com.example.jobbug.domain.user.converter;

import com.example.jobbug.domain.user.dto.response.UserInfoResponse;
import com.example.jobbug.domain.user.dto.response.UserRegisterResponse;
import com.example.jobbug.domain.user.entity.User;

public class UserConverter {
    public static UserRegisterResponse toUserRegisterResponse(User user, String accessToken) {
        return UserRegisterResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userId(user.getId())
                .profile(user.getProfile())
                .accessToken(accessToken)
                .build();
    }

    public static UserInfoResponse toUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfile())
                .build();
    }
}
