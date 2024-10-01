package com.example.jobbug.domain.auth.application;

import com.example.jobbug.domain.auth.dto.GoogleUserInfo;
import com.example.jobbug.domain.user.entity.User;
import com.example.jobbug.domain.user.repository.UserRepository;
import com.example.jobbug.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.redirect.access}")
    private String ACCESS_TOKEN_REDIRECT_URI; // 기존 유저 로그인 시 리다이렉트 URI

    @Value("${jwt.redirect.register}")
    private String REGISTER_TOKEN_REDIRECT_URI; // 신규 유저 로그인 시 리다이렉트 URI

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final String provider = token.getAuthorizedClientRegistrationId();

        // 구글 로그인 시도
        if (provider.equals("google")) {
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());

            String providerId = googleUserInfo.getProviderId();
            String email = googleUserInfo.getEmail();
            User existingUser = userRepository.findByProviderId(providerId);

            if (existingUser == null) {
                // 신규 유저
                log.info("신규 유저입니다.");
                String registerToken = jwtUtil.generateRegisterToken(providerId, email);
                String redirectUri = String.format(REGISTER_TOKEN_REDIRECT_URI, registerToken);
                getRedirectStrategy().sendRedirect(request, response, redirectUri);
            } else {
                // 기존 유저
                log.info("기존 유저입니다.");
                String accessToken = jwtUtil.generateAccessToken(existingUser.getId().toString());
                String redirectUri = String.format(ACCESS_TOKEN_REDIRECT_URI, accessToken);
                getRedirectStrategy().sendRedirect(request, response, redirectUri);
            }
        }
    }
}
