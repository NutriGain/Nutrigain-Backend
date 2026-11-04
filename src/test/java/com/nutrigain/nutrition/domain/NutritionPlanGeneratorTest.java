package com.nutrigain.nutrition.domain;

import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.valueobjects.Macros;
import com.nutrigain.nutrition.domain.model.valueobjects.MealType;
import com.nutrigain.nutrition.domain.model.valueobjects.NutritionFocus;
import com.nutrigain.nutrition.domain.model.valueobjects.PlanEntry;
import com.nutrigain.nutrition.domain.services.NutritionPlanGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NutritionPlanGeneratorTest {

    private Meal meal(long id, MealType type, int calories, int proteins, int carbs) {
        Meal meal = new Meal("meal-" + id, type, new Macros(calories, proteins, carbs, 5), null);
        ReflectionTestUtils.setField(meal, "id", id);
        return meal;
    }

    @Test
    void highProteinFocusStartsWithTheMostProteicMeal() {
        List<Meal> catalog = List.of(
                meal(1, MealType.LUNCH, 500, 20, 50),
                meal(2, MealType.LUNCH, 600, 40, 30));
        List<PlanEntry> plan = NutritionPlanGenerator.generate(NutritionFocus.HIGH_PROTEIN, catalog);
        PlanEntry monday = plan.stream().filter(e -> e.dayOfWeek() == DayOfWeek.MONDAY).findFirst().orElseThrow();
        assertEquals(2L, monday.mealId());
    }

    @Test
    void lightFocusStartsWithTheLowestCalorieMeal() {
        List<Meal> catalog = List.of(
                meal(1, MealType.DINNER, 700, 20, 50),
                meal(2, MealType.DINNER, 300, 10, 30));
        List<PlanEntry> plan = NutritionPlanGenerator.generate(NutritionFocus.LIGHT, catalog);
        PlanEntry monday = plan.stream().filter(e -> e.dayOfWeek() == DayOfWeek.MONDAY).findFirst().orElseThrow();
        assertEquals(2L, monday.mealId());
    }

    @Test
    void coversTheWholeWeekForEveryTypeInTheCatalog() {
        List<Meal> catalog = List.of(
                meal(1, MealType.BREAKFAST, 300, 10, 40),
                meal(2, MealType.SNACK, 150, 5, 20));
        List<PlanEntry> plan = NutritionPlanGenerator.generate(NutritionFocus.LIGHT, catalog);
        assertEquals(14, plan.size());
        assertTrue(plan.stream().noneMatch(e -> e.mealType() == MealType.LUNCH));
    }
}
