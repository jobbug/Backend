package com.example.jobbug.domain.user.dto.response;

import lombok.Builder;

@Builder
public class UserRegisterResponse {
    private Long userId;
    private String name;
    private String nickname;
    private String email;
    private String profile;
    private String accessToken;
}
