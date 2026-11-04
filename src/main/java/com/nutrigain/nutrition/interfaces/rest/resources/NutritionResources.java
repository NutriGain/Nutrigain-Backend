package com.nutrigain.nutrition.interfaces.rest.resources;

import com.nutrigain.nutrition.domain.model.valueobjects.MealType;

import java.time.DayOfWeek;
import java.util.List;

/** Response shapes of the Nutrition context. */
public final class NutritionResources {

    private NutritionResources() {
    }

    public record MealResource(Long id, String name, MealType mealType, int calories, int proteins,
                               int carbohydrates, int fats, String imageUrl) {
    }

    public record MacrosResource(int calories, int proteins, int carbohydrates, int fats) {
    }

    public record PlannedMealResource(MealType mealType, MealResource meal) {
    }

    public record PlanDayResource(DayOfWeek dayOfWeek, List<PlannedMealResource> meals) {
    }

    public record NutritionPlanResource(String focus, List<PlanDayResource> days) {
    }

    public record ManualPlanResource(List<MealResource> meals, MacrosResource totals) {
    }
}
