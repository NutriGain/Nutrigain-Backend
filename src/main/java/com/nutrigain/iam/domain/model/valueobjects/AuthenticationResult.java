package com.nutrigain.iam.domain.model.valueobjects;

import com.nutrigain.iam.domain.model.aggregates.User;

public record AuthenticationResult(User user, String token) {
}
