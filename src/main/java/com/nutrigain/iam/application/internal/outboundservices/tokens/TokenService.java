package com.nutrigain.iam.application.internal.outboundservices.tokens;

import java.util.Optional;

public interface TokenService {
    String generateToken(Long userId, String email);

    /** Returns the claims of a valid token, or empty if the token is invalid or expired. */
    Optional<TokenClaims> validate(String token);

    record TokenClaims(Long userId, String email) {
    }
}
