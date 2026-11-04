package com.nutrigain.gamification.domain.model.aggregates;

import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

/** Consecutive days with at least one rewarded activity. */
@Entity
@Table(name = "streaks")
public class Streak extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private int currentDays;

    @Column(nullable = false)
    private int longestDays;

    private LocalDate lastActivityDate;

    protected Streak() {
    }

    public Streak(Long userId) {
        this.userId = userId;
    }

    public void recordActivity(LocalDate date) {
        if (lastActivityDate == null) {
            currentDays = 1;
        } else if (date.isEqual(lastActivityDate) || date.isBefore(lastActivityDate)) {
            return;
        } else if (date.isEqual(lastActivityDate.plusDays(1))) {
            currentDays++;
        } else {
            currentDays = 1;
        }
        lastActivityDate = date;
        longestDays = Math.max(longestDays, currentDays);
    }

    /** The streak that is still alive on the given day (broken if the last activity was before yesterday). */
    public int currentDaysAsOf(LocalDate today) {
        if (lastActivityDate == null || lastActivityDate.isBefore(today.minusDays(1))) {
            return 0;
        }
        return currentDays;
    }

    public Long getUserId() {
        return userId;
    }

    public int getCurrentDays() {
        return currentDays;
    }

    public int getLongestDays() {
        return longestDays;
    }

    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }
}
