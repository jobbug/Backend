package com.example.jobbug.domain.user.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private String addr;
    private String detailAddr;
    private String profile;
    private String role;
}
