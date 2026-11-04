package com.nutrigain.iam.domain.model.events;

import com.nutrigain.shared.domain.model.DomainEvent;

/** Published language of IAM: other contexts react to it to bootstrap data for the new user. */
public record UserRegisteredEvent(Long userId, String name, Long fitnessPathId) implements DomainEvent {
}
