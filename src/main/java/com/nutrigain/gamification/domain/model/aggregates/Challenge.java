package com.nutrigain.gamification.domain.model.aggregates;

import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;
import com.nutrigain.gamification.domain.model.valueobjects.Difficulty;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/** A challenge of the catalog. */
@Entity
@Table(name = "challenges")
public class Challenge extends AggregateRoot {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 400)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChallengeCategory category;

    @Column(nullable = false)
    private int xpReward;

    protected Challenge() {
    }

    public Challenge(String title, String description, Difficulty difficulty, ChallengeCategory category, int xpReward) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.xpReward = xpReward;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public ChallengeCategory getCategory() {
        return category;
    }

    public int getXpReward() {
        return xpReward;
    }
}
