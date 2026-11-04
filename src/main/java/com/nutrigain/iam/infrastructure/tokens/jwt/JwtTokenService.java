package com.nutrigain.iam.infrastructure.tokens.jwt;

import com.nutrigain.iam.application.internal.outboundservices.tokens.TokenService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenService implements TokenService {

    private final SecretKey key;
    private final Duration expiration;
    private final Clock clock;

    public JwtTokenService(@Value("${nutrigain.jwt.secret}") String secret,
                           @Value("${nutrigain.jwt.expiration-minutes}") long expirationMinutes,
                           Clock clock) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofMinutes(expirationMinutes);
        this.clock = clock;
    }

    @Override
    public String generateToken(Long userId, String email) {
        Instant now = clock.instant();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(key)
                .compact();
    }

    @Override
    public Optional<TokenClaims> validate(String token) {
        try {
            var claims = Jwts.parser().verifyWith(key).clock(() -> Date.from(clock.instant()))
                    .build().parseSignedClaims(token).getPayload();
            return Optional.of(new TokenClaims(Long.valueOf(claims.getSubject()), claims.get("email", String.class)));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
