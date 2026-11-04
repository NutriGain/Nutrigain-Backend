package com.nutrigain.shared.infrastructure.security;

/** Principal placed in the SecurityContext once a JWT is validated. */
public record AuthenticatedUser(Long userId, String email) {
}
