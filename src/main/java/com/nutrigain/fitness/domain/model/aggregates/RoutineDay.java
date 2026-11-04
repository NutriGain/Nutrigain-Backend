package com.nutrigain.fitness.domain.model.aggregates;

import com.nutrigain.fitness.domain.model.valueobjects.Exercise;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/** One training day of a user's weekly routine. */
@Entity
@Table(name = "routine_days",
        uniqueConstraints = @UniqueConstraint(name = "uk_routine_user_day", columnNames = {"user_id", "day_of_week"}))
public class RoutineDay extends AggregateRoot {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 12)
    private DayOfWeek dayOfWeek;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "routine_day_exercises", joinColumns = @JoinColumn(name = "routine_day_id"))
    @OrderColumn(name = "position")
    private List<Exercise> exercises = new ArrayList<>();

    protected RoutineDay() {
    }

    public RoutineDay(Long userId, DayOfWeek dayOfWeek, List<Exercise> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("A routine day needs at least one exercise");
        }
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.exercises = new ArrayList<>(exercises);
    }

    public Long getUserId() {
        return userId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public List<Exercise> getExercises() {
        return List.copyOf(exercises);
    }
}
