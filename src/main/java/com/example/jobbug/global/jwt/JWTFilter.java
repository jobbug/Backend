package com.example.jobbug.global.jwt;

import com.example.jobbug.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && jwtUtil.isAccessTokenValid(token)) {
            String userId = jwtUtil.getUserIdFromToken(token).toString();

            // 인증 객체 생성 및 SecurityContext에 설정
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    userId, // principal로 이메일 사용
                    null,  // credentials는 필요 없으므로 null
                    null   // authorities는 비워둠 (필요한 경우 권한 추가)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 특정 경로는 필터링하지 않도록 설정
        String path = request.getRequestURI();
        return path.startsWith("/auth/google/") || path.startsWith("/api/user/register");
    }

    private String resolveToken(HttpServletRequest request) {
        // 요청 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
