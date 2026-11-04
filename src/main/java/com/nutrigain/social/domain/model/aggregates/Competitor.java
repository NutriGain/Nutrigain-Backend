package com.nutrigain.social.domain.model.aggregates;

import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/** Social read model of a user: what other users can see about them in rankings. */
@Entity
@Table(name = "competitors")
public class Competitor extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String displayName;

    @Column(nullable = false)
    private int experience;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private int currentStreak;

    protected Competitor() {
    }

    public Competitor(Long userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public void updateScore(int experience, int level, int currentStreak) {
        this.experience = experience;
        this.level = level;
        this.currentStreak = currentStreak;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }
}
