package com.nutrigain.nutrition.domain.model.aggregates;

import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

/** The meals a user hand-picked for their manual plan. One per user. */
@Entity
@Table(name = "manual_meal_selections")
public class ManualMealSelection extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "manual_meal_selection_meals", joinColumns = @JoinColumn(name = "selection_id"))
    @Column(name = "meal_id")
    private Set<Long> mealIds = new LinkedHashSet<>();

    protected ManualMealSelection() {
    }

    public ManualMealSelection(Long userId) {
        this.userId = userId;
    }

    public void add(Long mealId) {
        if (!mealIds.add(mealId)) {
            throw new BusinessRuleViolationException("The meal is already in the manual plan");
        }
    }

    public void remove(Long mealId) {
        if (!mealIds.remove(mealId)) {
            throw new BusinessRuleViolationException("The meal is not in the manual plan");
        }
    }

    public Long getUserId() {
        return userId;
    }

    public Set<Long> getMealIds() {
        return Set.copyOf(mealIds);
    }
}
