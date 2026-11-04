package com.nutrigain.gamification.domain.model.aggregates;

import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "motivational_messages")
public class MotivationalMessage extends AggregateRoot {

    @Column(nullable = false, length = 300)
    private String text;

    protected MotivationalMessage() {
    }

    public MotivationalMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
