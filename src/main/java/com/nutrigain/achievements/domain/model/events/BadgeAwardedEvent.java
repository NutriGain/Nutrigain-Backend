package com.nutrigain.achievements.domain.model.events;

import com.nutrigain.shared.domain.model.DomainEvent;

public record BadgeAwardedEvent(Long userId, String badgeCode, String badgeTitle) implements DomainEvent {
}
