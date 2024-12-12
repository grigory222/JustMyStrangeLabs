package web.backend.lab4.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@ApplicationScoped
public class JwtProvider {
    // DEBUG VALUE RN
    public static final int ACCESS_TOKEN_EXPIRATION = 2 * 60 * 60; // 2 minutes
    public static final int REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60; // 24 hours

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtProvider() {
        String secretKey = AuthConfig.getSecretKey();
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm).build(); // Создаем JWTVerifier для проверки подписи
    }

    public String generateAccessToken(String username, Long userId) {
        return generateToken(username, userId, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String username, Long userId) {
        return generateToken(username, userId, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(String username, Long userId, int time) {
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withExpiresAt(Date.from(Instant.now().plus(time, ChronoUnit.SECONDS)))
                .sign(algorithm);
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = verifier.verify(token); // Проверка подписи токена
        return jwt.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        DecodedJWT jwt = verifier.verify(token); // Проверка подписи токена
        return jwt.getClaim("userId").asLong();
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token); // Проверка подписи токена
            Date expirationTime = jwt.getExpiresAt();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (JWTVerificationException exception) {
            log.error("Invalid token signature or expired token: {}", exception.getMessage());
            return true;
        }
    }
}
