package com.wishlist.ai.security;

import com.wishlist.ai.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final AppProperties appProperties;

    public JwtService(AppProperties appProperties) {
        this.appProperties = appProperties;
        var secret = appProperties.jwt().secret();
        var padded = secret.length() >= 64 ? secret : (secret + "x".repeat(64 - secret.length()));
        var base64 = Base64.getEncoder().encodeToString(padded.getBytes(StandardCharsets.UTF_8));
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64));
    }

    public String generateToken(AppUserDetails user) {
        var expiry = Instant.now().plus(appProperties.jwt().expirationMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("fullName", user.getFullName())
                .issuedAt(new Date())
                .expiration(Date.from(expiry))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, AppUserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
