package web.backend.lab4.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@ApplicationScoped
public class JwtProvider {
    private static final int ACCESS_TOKEN_EXPIRATION = 2; // 2 минуты
    private static final int REFRESH_TOKEN_EXPIRATION = 1; // 1 день

    public String generateAccessToken(String username, Long userId) {
        return generateToken(username, userId, ACCESS_TOKEN_EXPIRATION, ChronoUnit.MINUTES);
    }

    public String generateRefreshToken(String username, Long userId) {
        return generateToken(username, userId, REFRESH_TOKEN_EXPIRATION, ChronoUnit.DAYS);
    }

    private String generateToken(String username, Long userId, int time, ChronoUnit chronoUnit){
        String secretKey = AuthConfig.getSecretKey();
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withExpiresAt(Instant.now().plus(time, chronoUnit))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTDecodeException exception) {
            log.error("Error decoding username from token: {}", exception.getMessage());
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException exception) {
            log.error("Error decoding user id from token: {}", exception.getMessage());

            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expirationTime = jwt.getExpiresAt();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (JWTDecodeException exception) {
            log.error("Error decoding expiration from token: {}", exception.getMessage());
            return true;
        }
    }
}
