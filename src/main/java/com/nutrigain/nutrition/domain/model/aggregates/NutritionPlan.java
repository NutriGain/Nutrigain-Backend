package com.nutrigain.nutrition.domain.model.aggregates;

import com.nutrigain.nutrition.domain.model.valueobjects.NutritionFocus;
import com.nutrigain.nutrition.domain.model.valueobjects.PlanEntry;
import com.nutrigain.shared.domain.model.AggregateRoot;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** The automatic weekly nutrition plan generated for a user. One per user. */
@Entity
@Table(name = "nutrition_plans")
public class NutritionPlan extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NutritionFocus focus;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "nutrition_plan_entries", joinColumns = @JoinColumn(name = "nutrition_plan_id"))
    private List<PlanEntry> entries = new ArrayList<>();

    protected NutritionPlan() {
    }

    public NutritionPlan(Long userId, NutritionFocus focus, List<PlanEntry> entries) {
        this.userId = userId;
        this.focus = focus;
        this.entries = new ArrayList<>(entries);
    }

    public Long getUserId() {
        return userId;
    }

    public NutritionFocus getFocus() {
        return focus;
    }

    public List<PlanEntry> getEntries() {
        return List.copyOf(entries);
    }
}
