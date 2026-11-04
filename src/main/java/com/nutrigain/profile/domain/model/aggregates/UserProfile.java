package com.nutrigain.profile.domain.model.aggregates;

import com.nutrigain.profile.domain.model.valueobjects.ActivityLevel;
import com.nutrigain.profile.domain.model.valueobjects.Sex;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private int heightCm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private ActivityLevel activityLevel;

    protected UserProfile() {
    }

    public UserProfile(Long userId, int heightCm, Sex sex, ActivityLevel activityLevel) {
        this.userId = userId;
        update(heightCm, sex, activityLevel);
    }

    public void update(int heightCm, Sex sex, ActivityLevel activityLevel) {
        if (heightCm < 100 || heightCm > 250) {
            throw new IllegalArgumentException("Height must be between 100 and 250 cm");
        }
        if (sex == null || activityLevel == null) {
            throw new IllegalArgumentException("Sex and activity level are required");
        }
        this.heightCm = heightCm;
        this.sex = sex;
        this.activityLevel = activityLevel;
    }

    public Long getUserId() {
        return userId;
    }

    public int getHeightCm() {
        return heightCm;
    }

    public Sex getSex() {
        return sex;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }
}
