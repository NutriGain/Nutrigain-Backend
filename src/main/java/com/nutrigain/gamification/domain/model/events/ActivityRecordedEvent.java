package com.nutrigain.gamification.domain.model.events;

import com.nutrigain.shared.domain.model.DomainEvent;

import java.time.LocalDate;

/** Published language of Gamification: the avatar and streak of a user changed after a rewarded activity. */
public record ActivityRecordedEvent(Long userId, int totalExperience, int level, boolean leveledUp,
                                    int currentStreak, LocalDate date) implements DomainEvent {
}
