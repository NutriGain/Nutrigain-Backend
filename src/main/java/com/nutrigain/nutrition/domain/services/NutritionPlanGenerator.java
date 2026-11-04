package com.nutrigain.nutrition.domain.services;

import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.valueobjects.MealType;
import com.nutrigain.nutrition.domain.model.valueobjects.NutritionFocus;
import com.nutrigain.nutrition.domain.model.valueobjects.PlanEntry;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Pure domain service: builds a 7-day plan from the catalog. Meals of each type are ranked according to the
 * user's focus and rotated across the week so that days do not repeat while the catalog allows it.
 */
public final class NutritionPlanGenerator {

    private NutritionPlanGenerator() {
    }

    public static List<PlanEntry> generate(NutritionFocus focus, List<Meal> catalog) {
        List<PlanEntry> entries = new ArrayList<>();
        for (MealType type : MealType.values()) {
            List<Meal> ranked = catalog.stream()
                    .filter(m -> m.getMealType() == type)
                    .sorted(comparatorFor(focus).thenComparing(Meal::getId))
                    .toList();
            if (ranked.isEmpty()) {
                continue;
            }
            DayOfWeek[] days = DayOfWeek.values();
            for (int i = 0; i < days.length; i++) {
                entries.add(new PlanEntry(days[i], type, ranked.get(i % ranked.size()).getId()));
            }
        }
        return entries;
    }

    private static Comparator<Meal> comparatorFor(NutritionFocus focus) {
        return switch (focus) {
            case HIGH_PROTEIN -> Comparator.comparingInt((Meal m) -> m.getMacros().proteins()).reversed();
            case HIGH_CARB -> Comparator.comparingInt((Meal m) -> m.getMacros().carbohydrates()).reversed();
            case LIGHT -> Comparator.comparingInt((Meal m) -> m.getMacros().calories());
        };
    }
}
