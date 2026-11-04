package com.nutrigain.nutrition.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Macros(
        @Column(name = "calories", nullable = false) int calories,
        @Column(name = "proteins_g", nullable = false) int proteins,
        @Column(name = "carbohydrates_g", nullable = false) int carbohydrates,
        @Column(name = "fats_g", nullable = false) int fats) {

    public Macros {
        if (calories < 0 || proteins < 0 || carbohydrates < 0 || fats < 0) {
            throw new IllegalArgumentException("Macros cannot be negative");
        }
    }

    public static Macros zero() {
        return new Macros(0, 0, 0, 0);
    }

    public Macros plus(Macros other) {
        return new Macros(calories + other.calories, proteins + other.proteins,
                carbohydrates + other.carbohydrates, fats + other.fats);
    }
}
