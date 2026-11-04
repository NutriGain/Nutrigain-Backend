package com.nutrigain.nutrition.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.DayOfWeek;

/** One meal slot of the weekly plan: which meal is eaten on which day as which meal type. */
@Embeddable
public record PlanEntry(
        @Enumerated(EnumType.STRING) @Column(name = "day_of_week", nullable = false, length = 12) DayOfWeek dayOfWeek,
        @Enumerated(EnumType.STRING) @Column(name = "meal_type", nullable = false, length = 12) MealType mealType,
        @Column(name = "meal_id", nullable = false) Long mealId) {
}
