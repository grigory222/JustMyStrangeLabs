package ru.ifmo.se.network;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import ru.ifmo.se.util.PropertiesUtil;

import java.util.Date;

public class JwtManager {
    private final Algorithm algorithm = Algorithm.HMAC384(PropertiesUtil.getProperty("jwt.secret_key"));
    private final JWTVerifier verifier = JWT.require(algorithm)
                                            .withIssuer("serv")
                                            .build();

    public String generateJwtToken(long id) {
        try {
            String token = JWT.create()
                    .withIssuer("serv")
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 1 week
                    .withClaim("id", id)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            return null;
        }
    }

    public long decodeJwtToken(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("id").asLong();
        } catch (JWTVerificationException exception){
            return -1;
        }
    }
}
