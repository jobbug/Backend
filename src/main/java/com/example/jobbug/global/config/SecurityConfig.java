package com.example.jobbug.global.config;

import com.example.jobbug.domain.auth.application.OAuthLoginFailureHandler;
import com.example.jobbug.domain.auth.application.OAuthLoginSuccessHandler;
import com.example.jobbug.domain.auth.service.CustomOAuth2UserService;
import com.example.jobbug.global.jwt.JWTFilter;
import com.example.jobbug.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * 권한별 URI
     */

    // 관리자 권한
    private final String[] adminUrl = {"/admin/**"};

    // 아무나 접근 가능한 URI
    private final String[] permitAllUrl = {
            "/api/user/register", "/auth/google/**"
    };

//    // 인증되지 않은 사용자만 접근 가능한 URI
//    private final String[] anonymousUrl = {
//
//    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 비활성화 (JWT 사용)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 비활성화
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .authorizeHttpRequests(auth -> auth // 접근 uri 권한 관리.requestMatchers("/auth/google/**").permitAll()
                        .requestMatchers(adminUrl).hasAnyRole("ADMIN")
                        .requestMatchers(permitAllUrl).permitAll()
//                        .requestMatchers(anonymousUrl).anonymous()
                        .anyRequest().authenticated() // 이 외의 url은 인증받은 사용자만 접근 가능
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuthLoginSuccessHandler) // OAuth2 로그인 성공 핸들러 설정
                        .failureHandler(oAuthLoginFailureHandler) // OAuth2 로그인 실패 핸들러 설정
                )
                .addFilterBefore(new JWTFilter(jwtUtil, customOAuth2UserService), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 프론트엔드 URL
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
