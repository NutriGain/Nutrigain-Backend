package com.nutrigain.notifications.domain.model.aggregates;

import com.nutrigain.notifications.domain.model.valueobjects.NotificationType;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/** An in-app notification in the inbox of a user. */
@Entity
@Table(name = "notifications")
public class Notification extends AggregateRoot {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private NotificationType type;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 400)
    private String message;

    @Column(name = "is_seen", nullable = false)
    private boolean seen;

    protected Notification() {
    }

    public Notification(Long userId, NotificationType type, String title, String message) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public void markSeen() {
        this.seen = true;
    }

    public Long getUserId() {
        return userId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSeen() {
        return seen;
    }
}
