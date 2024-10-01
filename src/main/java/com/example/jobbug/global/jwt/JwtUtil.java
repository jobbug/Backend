package com.example.jobbug.global.jwt;

import com.example.jobbug.global.exception.enums.ErrorCode;
import com.example.jobbug.global.exception.model.TokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰 발급 메서드
    public String generateAccessToken(String userId) {
        log.info("액세스 토큰이 발행되었습니다.");
        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(this.getSigningKey())
                .compact();
    }

    // 추가 정보 입력 시 사용할 등록 토큰 발급 메서드
    public String generateRegisterToken(String providerId, String email) {
        log.info("레지스터 토큰이 발행되었습니다.");
        return Jwts.builder()
                .claim("providerId", providerId)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(this.getSigningKey())
                .compact();
    }

    // 액세스 토큰 유효성 검사
    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, "userId");
    }

    // 레지스터 토큰 유효성 검사
    public boolean isRegisterTokenValid(String token) {
        return isTokenValid(token, "providerId");
    }

    // 공통 유효성 검사 로직
    private boolean isTokenValid(String token, String claimKey) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            // 토큰 만료 여부 확인
            Date expirationDate = claims.getPayload().getExpiration();
            if (expirationDate.before(new Date())) {
                log.warn("토큰이 만료되었습니다.");
                return false; // 만료된 토큰
            }

            // 필수 클레임이 있는지 확인
            String claimValue = claims.getPayload().get(claimKey, String.class);
            if (claimValue == null || claimValue.isEmpty()) {
                log.warn("토큰에 {} 클레임이 없습니다.", claimKey);
                return false; // 필수 클레임이 없는 경우
            }

            return true; // 유효한 토큰
        } catch (JwtException e) {
            log.warn("유효하지 않은 토큰입니다. JWT 예외: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 토큰 형식입니다. 예외: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 providerId를 추출하는 메서드
    public String getProviderIdFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("providerId", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 토큰입니다.");
            throw new TokenException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }

    // 토큰에서 email을 추출하는 메서드
    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("email", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 토큰입니다.");
            throw new TokenException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }

    // 토큰에서 userId를 추출하는 메서드
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 토큰입니다.");
            throw new TokenException(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }
    }
}
