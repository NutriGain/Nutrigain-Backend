package com.nutrigain.achievements.domain.model.aggregates;

import com.nutrigain.achievements.domain.model.valueobjects.BadgeType;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.EnumSet;
import java.util.Set;

/** Counters of a user's activity. The rules that unlock each badge live here. */
@Entity
@Table(name = "achievement_trackers")
public class AchievementTracker extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private int workoutsCompleted;

    @Column(nullable = false)
    private int challengesCompleted;

    @Column(nullable = false)
    private int extremeCompleted;

    @Column(nullable = false)
    private int longestStreak;

    @Column(nullable = false)
    private int level = 1;

    protected AchievementTracker() {
    }

    public AchievementTracker(Long userId) {
        this.userId = userId;
    }

    public void workoutCompleted() {
        workoutsCompleted++;
    }

    public void challengeCompleted(boolean extreme) {
        challengesCompleted++;
        if (extreme) {
            extremeCompleted++;
        }
    }

    public void activityStats(int currentStreak, int level) {
        this.longestStreak = Math.max(longestStreak, currentStreak);
        this.level = Math.max(this.level, level);
    }

    /** Every badge whose condition is currently satisfied. */
    public Set<BadgeType> earnedBadges() {
        Set<BadgeType> earned = EnumSet.noneOf(BadgeType.class);
        if (workoutsCompleted >= 1) {
            earned.add(BadgeType.FIRST_WORKOUT);
        }
        if (workoutsCompleted >= 10) {
            earned.add(BadgeType.TEN_WORKOUTS);
        }
        if (challengesCompleted >= 1) {
            earned.add(BadgeType.FIRST_CHALLENGE);
        }
        if (extremeCompleted >= 1) {
            earned.add(BadgeType.EXTREME_CHAMPION);
        }
        if (longestStreak >= 7) {
            earned.add(BadgeType.WEEK_STREAK);
        }
        if (longestStreak >= 30) {
            earned.add(BadgeType.MONTH_STREAK);
        }
        if (level >= 5) {
            earned.add(BadgeType.LEVEL_5);
        }
        return earned;
    }

    public Long getUserId() {
        return userId;
    }
}
