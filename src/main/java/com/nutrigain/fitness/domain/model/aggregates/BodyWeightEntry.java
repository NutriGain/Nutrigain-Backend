package com.nutrigain.fitness.domain.model.aggregates;

import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "body_weight_entries",
        uniqueConstraints = @UniqueConstraint(name = "uk_weight_user_date", columnNames = {"user_id", "recorded_on"}))
public class BodyWeightEntry extends AggregateRoot {

    private static final BigDecimal MIN_KG = BigDecimal.valueOf(20);
    private static final BigDecimal MAX_KG = BigDecimal.valueOf(500);

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recorded_on", nullable = false)
    private LocalDate recordedOn;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weightKg;

    protected BodyWeightEntry() {
    }

    public BodyWeightEntry(Long userId, LocalDate recordedOn, BigDecimal weightKg) {
        this.userId = userId;
        this.recordedOn = recordedOn;
        changeWeight(weightKg);
    }

    public void changeWeight(BigDecimal weightKg) {
        if (weightKg == null || weightKg.compareTo(MIN_KG) < 0 || weightKg.compareTo(MAX_KG) > 0) {
            throw new IllegalArgumentException("Weight must be between 20 and 500 kg");
        }
        this.weightKg = weightKg;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getRecordedOn() {
        return recordedOn;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }
}
