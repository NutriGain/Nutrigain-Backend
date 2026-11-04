package com.nutrigain.iam.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record EmailAddress(@Column(name = "email", nullable = false, length = 150) String value) {

    private static final Pattern PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public EmailAddress {
        if (value == null || !PATTERN.matcher(value.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
        value = value.trim().toLowerCase();
    }
}
