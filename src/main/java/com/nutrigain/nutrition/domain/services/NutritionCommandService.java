package com.nutrigain.nutrition.domain.services;

import com.nutrigain.nutrition.domain.model.aggregates.ManualMealSelection;
import com.nutrigain.nutrition.domain.model.aggregates.NutritionPlan;
import com.nutrigain.nutrition.domain.model.commands.AddMealToManualSelectionCommand;
import com.nutrigain.nutrition.domain.model.commands.GenerateNutritionPlanCommand;
import com.nutrigain.nutrition.domain.model.commands.RemoveMealFromManualSelectionCommand;

public interface NutritionCommandService {
    NutritionPlan handle(GenerateNutritionPlanCommand command);

    ManualMealSelection handle(AddMealToManualSelectionCommand command);

    ManualMealSelection handle(RemoveMealFromManualSelectionCommand command);
}
