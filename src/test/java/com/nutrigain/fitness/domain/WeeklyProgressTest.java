package com.nutrigain.fitness.domain;

import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import com.nutrigain.fitness.domain.model.events.WorkoutCompletedEvent;
import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeeklyProgressTest {

    private final LocalDate monday = LocalDate.of(2026, 11, 2);

    @Test
    void percentageFollowsCompletedWorkouts() {
        WeeklyProgress progress = new WeeklyProgress(1L, monday, 4, null);
        progress.completeWorkout(10L, monday);
        assertEquals(25, progress.completionPercentage());
        progress.completeWorkout(11L, monday);
        assertEquals(50, progress.completionPercentage());
    }

    @Test
    void completingAWorkoutRaisesAnEvent() {
        WeeklyProgress progress = new WeeklyProgress(1L, monday, 3, null);
        progress.completeWorkout(10L, monday);
        var events = progress.pullDomainEvents();
        assertEquals(1, events.size());
        assertInstanceOf(WorkoutCompletedEvent.class, events.get(0));
        assertEquals(0, progress.pullDomainEvents().size());
    }

    @Test
    void sameWorkoutCannotBeCompletedTwiceInAWeek() {
        WeeklyProgress progress = new WeeklyProgress(1L, monday, 3, null);
        progress.completeWorkout(10L, monday);
        assertThrows(BusinessRuleViolationException.class, () -> progress.completeWorkout(10L, monday));
    }

    @Test
    void noPlannedWorkoutsMeansZeroPercent() {
        assertEquals(0, new WeeklyProgress(1L, monday, 0, null).completionPercentage());
    }
}
