package com.example.jobbug.domain.user.converter;

import com.example.jobbug.domain.user.dto.response.GetUserProfileResponse;
import com.example.jobbug.domain.user.dto.response.UserInfoResponse;
import com.example.jobbug.domain.user.dto.response.UserRegisterResponse;
import com.example.jobbug.domain.user.entity.User;

import java.util.stream.Collectors;

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
                .phone(user.getPhone())
                .addr(user.getAddr())
                .detailAddr(user.getDetail_addr())
                .build();
    }

    public static GetUserProfileResponse toGetUserProfileResponse(User user, int totalRequests, int successfulAcceptances) {
        return GetUserProfileResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .addr(user.getAddr())
                .totalRequests(totalRequests)
                .successfulAcceptances(successfulAcceptances)
                .badges(user.getBadges().stream()
                        .map(badge -> GetUserProfileResponse.Badge.builder()
                                .name(badge.getName())
                                .type(badge.getType())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
