package com.nutrigain.nutrition.domain.services;

import com.nutrigain.nutrition.domain.model.aggregates.ManualMealSelection;
import com.nutrigain.nutrition.domain.model.aggregates.NutritionPlan;
import com.nutrigain.nutrition.domain.model.queries.GetManualSelectionByUserIdQuery;
import com.nutrigain.nutrition.domain.model.queries.GetNutritionPlanByUserIdQuery;

import java.util.Optional;

public interface NutritionQueryService {
    Optional<NutritionPlan> handle(GetNutritionPlanByUserIdQuery query);

    Optional<ManualMealSelection> handle(GetManualSelectionByUserIdQuery query);
}
