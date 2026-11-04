package com.nutrigain.achievements.domain.model.aggregates;

import com.nutrigain.achievements.domain.model.events.BadgeAwardedEvent;
import com.nutrigain.achievements.domain.model.valueobjects.BadgeType;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(name = "user_badges",
        uniqueConstraints = @UniqueConstraint(name = "uk_badge_user_type", columnNames = {"user_id", "badge"}))
public class UserBadge extends AggregateRoot {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BadgeType badge;

    @Column(nullable = false)
    private LocalDate awardedOn;

    protected UserBadge() {
    }

    private UserBadge(Long userId, BadgeType badge, LocalDate awardedOn) {
        this.userId = userId;
        this.badge = badge;
        this.awardedOn = awardedOn;
    }

    public static UserBadge award(Long userId, BadgeType badge, LocalDate date) {
        UserBadge userBadge = new UserBadge(userId, badge, date);
        userBadge.registerEvent(new BadgeAwardedEvent(userId, badge.name(), badge.title()));
        return userBadge;
    }

    public Long getUserId() {
        return userId;
    }

    public BadgeType getBadge() {
        return badge;
    }

    public LocalDate getAwardedOn() {
        return awardedOn;
    }
}
