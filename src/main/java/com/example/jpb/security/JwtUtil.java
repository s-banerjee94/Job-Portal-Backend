package com.example.jpb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT Utility class for token generation and validation
 * Implements RFC 7519 (JSON Web Token) standard
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long jwtExpirationMs;
    private final String issuer;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long jwtExpirationMs,
                   @Value("${jwt.issuer}") String issuer) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
        this.issuer = issuer;
    }

    /**
     * Generate JWT token with industry standard claims (RFC 7519)
     * <p>
     * Standard Claims:
     * - sub (subject): User ID
     * - iat (issued at): Token creation timestamp
     * - exp (expiration): Token expiration timestamp
     * - iss (issuer): Token issuer identifier
     * - jti (JWT ID): Unique token identifier
     * <p>
     * Custom Claims:
     * - email: User email address
     * - role: User role (ROLE_CANDIDATE or ROLE_RECRUITER)
     */
    public String generateToken(String email, String role, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .issuer(issuer)
                .id(UUID.randomUUID().toString())
                .claim("email", email)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    public String extractJwtId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String email) {
        try {
            final String extractedEmail = extractEmail(token);
            final String extractedIssuer = extractIssuer(token);

            return extractedEmail != null &&
                    extractedEmail.equals(email) &&
                    extractedIssuer.equals(issuer) &&
                    !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
