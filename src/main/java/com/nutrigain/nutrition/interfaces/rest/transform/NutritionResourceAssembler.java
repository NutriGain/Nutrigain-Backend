package com.nutrigain.nutrition.interfaces.rest.transform;

import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.aggregates.NutritionPlan;
import com.nutrigain.nutrition.domain.model.valueobjects.Macros;
import com.nutrigain.nutrition.domain.model.valueobjects.PlanEntry;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.MacrosResource;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.ManualPlanResource;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.MealResource;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.NutritionPlanResource;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.PlanDayResource;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.PlannedMealResource;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class NutritionResourceAssembler {
    private NutritionResourceAssembler() {
    }

    public static MealResource toResource(Meal meal) {
        Macros m = meal.getMacros();
        return new MealResource(meal.getId(), meal.getName(), meal.getMealType(), m.calories(), m.proteins(),
                m.carbohydrates(), m.fats(), meal.getImageUrl());
    }

    public static NutritionPlanResource toResource(NutritionPlan plan, List<Meal> meals) {
        Map<Long, Meal> byId = meals.stream().collect(Collectors.toMap(Meal::getId, Function.identity()));
        Map<DayOfWeek, List<PlanEntry>> byDay = plan.getEntries().stream()
                .collect(Collectors.groupingBy(PlanEntry::dayOfWeek));
        List<PlanDayResource> days = byDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new PlanDayResource(e.getKey(), e.getValue().stream()
                        .sorted(Comparator.comparing(PlanEntry::mealType))
                        .filter(entry -> byId.containsKey(entry.mealId()))
                        .map(entry -> new PlannedMealResource(entry.mealType(), toResource(byId.get(entry.mealId()))))
                        .toList()))
                .toList();
        return new NutritionPlanResource(plan.getFocus().name(), days);
    }

    public static ManualPlanResource toManualPlan(List<Meal> meals) {
        Macros total = meals.stream().map(Meal::getMacros).reduce(Macros.zero(), Macros::plus);
        return new ManualPlanResource(meals.stream().map(NutritionResourceAssembler::toResource).toList(),
                new MacrosResource(total.calories(), total.proteins(), total.carbohydrates(), total.fats()));
    }
}
