package com.nutrigain.fitness.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/** An exercise prescription: what to do, how many sets and the reps/duration of each set (e.g. "10", "30 s"). */
@Embeddable
public record Exercise(
        @Column(name = "exercise_name", nullable = false, length = 120) String name,
        @Column(name = "sets", nullable = false) int sets,
        @Column(name = "reps", nullable = false, length = 30) String reps) {

    public Exercise {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Exercise name is required");
        }
        if (sets < 1) {
            throw new IllegalArgumentException("Sets must be at least 1");
        }
    }
}
