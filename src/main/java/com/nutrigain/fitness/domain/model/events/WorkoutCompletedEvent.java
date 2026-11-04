package com.nutrigain.fitness.domain.model.events;

import com.nutrigain.shared.domain.model.DomainEvent;

import java.time.LocalDate;

/** Published language of Fitness: a user finished one of their routine days. */
public record WorkoutCompletedEvent(Long userId, Long routineDayId, LocalDate completedOn) implements DomainEvent {
}
