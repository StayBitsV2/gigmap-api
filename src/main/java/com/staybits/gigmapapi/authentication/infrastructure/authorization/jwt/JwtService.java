package com.staybits.gigmapapi.authentication.infrastructure.authorization.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * JWT Service for token generation and validation
 */
@Service
public class JwtService {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGeneration12345}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private Long expiration;

    /**
     * Generate JWT token for a user
     * @param userId User ID
     * @param email User email
     * @param username Username
     * @return JWT token
     */
    public String generateToken(Long userId, String email, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer("gigmap-api")
                .withSubject(userId.toString())
                .withClaim("email", email)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    /**
     * Validate JWT token
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("gigmap-api")
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * Extract user ID from token
     * @param token JWT token
     * @return User ID
     */
    public Long getUserIdFromToken(String token) {
        DecodedJWT jwt = decodeToken(token);
        return Long.parseLong(jwt.getSubject());
    }

    /**
     * Extract email from token
     * @param token JWT token
     * @return Email
     */
    public String getEmailFromToken(String token) {
        DecodedJWT jwt = decodeToken(token);
        return jwt.getClaim("email").asString();
    }

    /**
     * Extract username from token
     * @param token JWT token
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = decodeToken(token);
        return jwt.getClaim("username").asString();
    }

    /**
     * Decode JWT token
     * @param token JWT token
     * @return DecodedJWT
     */
    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("gigmap-api")
                .build()
                .verify(token);
    }
}
