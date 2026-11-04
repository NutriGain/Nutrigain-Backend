package com.nutrigain.shared.domain.exceptions;

/** Thrown when a command breaks a domain invariant (maps to 422). */
public class BusinessRuleViolationException extends RuntimeException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
