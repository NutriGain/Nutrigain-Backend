package com.nutrigain.nutrition.application.internal.queryservices;

import com.nutrigain.nutrition.domain.model.aggregates.ManualMealSelection;
import com.nutrigain.nutrition.domain.model.aggregates.NutritionPlan;
import com.nutrigain.nutrition.domain.model.queries.GetManualSelectionByUserIdQuery;
import com.nutrigain.nutrition.domain.model.queries.GetNutritionPlanByUserIdQuery;
import com.nutrigain.nutrition.domain.services.NutritionQueryService;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.ManualMealSelectionRepository;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.NutritionPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NutritionQueryServiceImpl implements NutritionQueryService {

    private final NutritionPlanRepository planRepository;
    private final ManualMealSelectionRepository selectionRepository;

    public NutritionQueryServiceImpl(NutritionPlanRepository planRepository,
                                     ManualMealSelectionRepository selectionRepository) {
        this.planRepository = planRepository;
        this.selectionRepository = selectionRepository;
    }

    @Override
    public Optional<NutritionPlan> handle(GetNutritionPlanByUserIdQuery query) {
        return planRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<ManualMealSelection> handle(GetManualSelectionByUserIdQuery query) {
        return selectionRepository.findByUserId(query.userId());
    }
}
