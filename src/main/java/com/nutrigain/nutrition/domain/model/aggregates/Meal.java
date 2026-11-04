package com.nutrigain.nutrition.domain.model.aggregates;

import com.nutrigain.nutrition.domain.model.valueobjects.Macros;
import com.nutrigain.nutrition.domain.model.valueobjects.MealType;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/** A meal of the catalog. */
@Entity
@Table(name = "meals")
public class Meal extends AggregateRoot {

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private MealType mealType;

    @Embedded
    private Macros macros;

    @Column(length = 500)
    private String imageUrl;

    protected Meal() {
    }

    public Meal(String name, MealType mealType, Macros macros, String imageUrl) {
        this.name = name;
        this.mealType = mealType;
        this.macros = macros;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Macros getMacros() {
        return macros;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
