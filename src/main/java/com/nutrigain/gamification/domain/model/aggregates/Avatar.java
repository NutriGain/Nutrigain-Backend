package com.nutrigain.gamification.domain.model.aggregates;

import com.nutrigain.gamification.domain.model.valueobjects.AvatarCharacter;
import com.nutrigain.gamification.domain.model.valueobjects.Mood;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/** The virtual companion of a user. It levels up as the user earns experience. */
@Entity
@Table(name = "avatars")
public class Avatar extends AggregateRoot {

    public static final int EXPERIENCE_PER_LEVEL = 100;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 60)
    private String characterName;

    @Column(length = 200)
    private String imageUrl;

    @Column(nullable = false)
    private int experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Mood mood;

    protected Avatar() {
    }

    public Avatar(Long userId, AvatarCharacter character) {
        this.userId = userId;
        this.characterName = character.name();
        this.imageUrl = character.imageUrl();
        this.experience = 0;
        this.mood = Mood.MOTIVATED;
    }

    public void gainExperience(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Experience must be positive");
        }
        this.experience += amount;
        this.mood = amount >= 50 ? Mood.ENERGETIC : Mood.MOTIVATED;
    }

    public int level() {
        return 1 + experience / EXPERIENCE_PER_LEVEL;
    }

    public int experienceToNextLevel() {
        return EXPERIENCE_PER_LEVEL - (experience % EXPERIENCE_PER_LEVEL);
    }

    public Long getUserId() {
        return userId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getExperience() {
        return experience;
    }

    public Mood getMood() {
        return mood;
    }
}
