package com.nutrigain.gamification.domain.model.events;

import com.nutrigain.shared.domain.model.DomainEvent;

import java.time.LocalDate;

public record ChallengeCompletedEvent(Long userId, Long assignmentId, int xpAwarded, boolean extreme,
                                      LocalDate completedOn) implements DomainEvent {
}
