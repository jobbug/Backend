package com.example.jobbug.domain.user.service;

import com.example.jobbug.domain.user.converter.UserConverter;
import com.example.jobbug.domain.user.dto.request.UserRegisterRequest;
import com.example.jobbug.domain.user.dto.response.UserRegisterResponse;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.model.TokenException;
import com.example.jobbug.global.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.jobbug.global.exception.enums.ErrorCode.INVALID_TOKEN_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 사용자 등록 메서드
    public UserRegisterResponse registerUser(String registerToken, UserRegisterRequest request) {
        // 레지스터 토큰 유효성 검사
        if (!jwtUtil.isRegisterTokenValid(registerToken)) {
            throw new TokenException(INVALID_TOKEN_EXCEPTION);
        }

        User newUser = User.builder()
                .providerId(jwtUtil.getProviderIdFromToken(registerToken))
                .email(jwtUtil.getEmailFromToken(registerToken))
                .name(request.getName())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .addr(request.getAddr())
                .detail_addr(request.getDetailAddr())
                .profile(request.getProfile())
                .role("ROLE_USER")
                .build();

        // 사용자 저장
        userRepository.save(newUser);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(newUser.getEmail());

        return UserConverter.toUserRegisterResponse(newUser, accessToken);
    }

    // 쿠키를 삭제하는 메서드
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");  // 삭제할 쿠키의 경로
        cookie.setMaxAge(0);  // 만료시간을 0으로 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // HTTPS 사용 시 true로 설정
        response.addCookie(cookie);
    }


}

