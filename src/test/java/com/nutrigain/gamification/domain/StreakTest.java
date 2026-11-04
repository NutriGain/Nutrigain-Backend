package com.nutrigain.gamification.domain;

import com.nutrigain.gamification.domain.model.aggregates.Streak;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreakTest {

    private final LocalDate monday = LocalDate.of(2026, 11, 2);

    @Test
    void firstActivityStartsTheStreak() {
        Streak streak = new Streak(1L);
        streak.recordActivity(monday);
        assertEquals(1, streak.getCurrentDays());
    }

    @Test
    void consecutiveDaysExtendTheStreak() {
        Streak streak = new Streak(1L);
        streak.recordActivity(monday);
        streak.recordActivity(monday.plusDays(1));
        streak.recordActivity(monday.plusDays(2));
        assertEquals(3, streak.getCurrentDays());
        assertEquals(3, streak.getLongestDays());
    }

    @Test
    void sameDayDoesNotCountTwice() {
        Streak streak = new Streak(1L);
        streak.recordActivity(monday);
        streak.recordActivity(monday);
        assertEquals(1, streak.getCurrentDays());
    }

    @Test
    void missingADayResetsCurrentButKeepsLongest() {
        Streak streak = new Streak(1L);
        streak.recordActivity(monday);
        streak.recordActivity(monday.plusDays(1));
        streak.recordActivity(monday.plusDays(3));
        assertEquals(1, streak.getCurrentDays());
        assertEquals(2, streak.getLongestDays());
    }

    @Test
    void streakIsDeadWhenLastActivityIsOlderThanYesterday() {
        Streak streak = new Streak(1L);
        streak.recordActivity(monday);
        assertEquals(1, streak.currentDaysAsOf(monday.plusDays(1)));
        assertEquals(0, streak.currentDaysAsOf(monday.plusDays(2)));
    }
}
