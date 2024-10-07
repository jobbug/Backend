package com.example.jobbug.domain.user.service;

import com.example.jobbug.domain.user.converter.UserConverter;
import com.example.jobbug.domain.user.dto.request.UserRegisterRequest;
import com.example.jobbug.domain.user.dto.response.UserInfoResponse;
import com.example.jobbug.domain.user.dto.response.UserRegisterResponse;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.DuplicateException;
import com.example.jobbug.global.exception.model.NotFoundException;
import com.example.jobbug.global.exception.model.S3Exception;
import com.example.jobbug.global.exception.model.TokenException;
import com.example.jobbug.global.jwt.JwtUtil;
import com.example.jobbug.global.s3.S3Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.jobbug.global.exception.enums.ErrorCode.INVALID_TOKEN_EXCEPTION;
import static com.example.jobbug.global.exception.enums.ErrorCode.S3_UPLOAD_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    // 회원가입
    @Transactional
    public UserRegisterResponse registerUser(String registerToken, MultipartFile profileImage, UserRegisterRequest request) {
        validateToken(registerToken);

        String providerId = jwtUtil.getProviderIdFromToken(registerToken);
        String email = jwtUtil.getEmailFromToken(registerToken);

        // 프로필 이미지 처리
        String profileImageUrl = handleProfileImage(profileImage);

        // 새로운 User 생성 및 저장
        User newUser = User.of(providerId, email, request, profileImageUrl);
        userRepository.save(newUser);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(newUser.getId().toString());

        return UserConverter.toUserRegisterResponse(newUser, accessToken);
    }

    // 로그아웃
    @Transactional
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");  // 삭제할 쿠키의 경로
        cookie.setMaxAge(0);  // 만료시간을 0으로 설정
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // HTTPS 사용 시 true로 설정
        response.addCookie(cookie);
    }

    // 유저 정보 조회
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_EXCEPTION));

        return UserConverter.toUserInfoResponse(user);
    }


    // 토큰 유효성 검사
    private void validateToken(String registerToken) {
        if (!jwtUtil.isRegisterTokenValid(registerToken)) {
            throw new TokenException(INVALID_TOKEN_EXCEPTION);
        }
    }

    // 프로필 이미지 처리
    private String handleProfileImage(MultipartFile profileImage) {
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                log.info("프로필 이미지를 S3에 업로드 중입니다...");
                String uploadedUrl = s3Service.upload(profileImage, "profile");
                log.info("프로필 이미지 업로드 성공: {}", uploadedUrl);
                return uploadedUrl;
            } catch (IllegalArgumentException | IOException e) {
                log.error("S3 업로드 중 오류 발생: {}", e.getMessage());
                throw new S3Exception(S3_UPLOAD_FAILED);
            }
        }
        // 기본 프로필 이미지 설정
        return "https://jobbug-bucket.s3.ap-northeast-2.amazonaws.com/defaultprofile.svg";
    }

    public void checkDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME_EXCEPTION);
        }
    }
}
