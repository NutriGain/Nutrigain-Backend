package com.nutrigain.fitness.domain.model.aggregates;

import com.nutrigain.fitness.domain.model.events.WorkoutCompletedEvent;
import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** Training progress of one user during one week (weeks start on Monday). */
@Entity
@Table(name = "weekly_progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_progress_user_week", columnNames = {"user_id", "week_start"}))
public class WeeklyProgress extends AggregateRoot {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Column(nullable = false)
    private int plannedWorkouts;

    @Column(precision = 5, scale = 2)
    private BigDecimal targetWeightKg;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "weekly_progress_completed_routines", joinColumns = @JoinColumn(name = "weekly_progress_id"))
    @Column(name = "routine_day_id")
    private Set<Long> completedRoutineDayIds = new HashSet<>();

    protected WeeklyProgress() {
    }

    public WeeklyProgress(Long userId, LocalDate weekStart, int plannedWorkouts, BigDecimal targetWeightKg) {
        this.userId = userId;
        this.weekStart = weekStart;
        this.plannedWorkouts = plannedWorkouts;
        this.targetWeightKg = targetWeightKg;
    }

    public void completeWorkout(Long routineDayId, LocalDate date) {
        if (!completedRoutineDayIds.add(routineDayId)) {
            throw new BusinessRuleViolationException("This workout was already completed this week");
        }
        registerEvent(new WorkoutCompletedEvent(userId, routineDayId, date));
    }

    public void synchronizePlannedWorkouts(int planned) {
        this.plannedWorkouts = planned;
    }

    public void changeTargetWeight(BigDecimal targetWeightKg) {
        if (targetWeightKg == null || targetWeightKg.signum() <= 0 || targetWeightKg.compareTo(BigDecimal.valueOf(500)) > 0) {
            throw new IllegalArgumentException("Target weight must be between 0 and 500 kg");
        }
        this.targetWeightKg = targetWeightKg;
    }

    public int completedWorkouts() {
        return completedRoutineDayIds.size();
    }

    public int completionPercentage() {
        if (plannedWorkouts <= 0) {
            return 0;
        }
        return Math.min(100, completedWorkouts() * 100 / plannedWorkouts);
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public int getPlannedWorkouts() {
        return plannedWorkouts;
    }

    public BigDecimal getTargetWeightKg() {
        return targetWeightKg;
    }

    public Set<Long> getCompletedRoutineDayIds() {
        return Set.copyOf(completedRoutineDayIds);
    }
}
