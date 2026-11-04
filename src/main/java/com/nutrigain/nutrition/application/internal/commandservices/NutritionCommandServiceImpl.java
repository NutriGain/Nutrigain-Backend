package com.nutrigain.nutrition.application.internal.commandservices;

import com.nutrigain.nutrition.application.internal.outboundservices.acl.ExternalFitnessService;
import com.nutrigain.nutrition.domain.model.aggregates.ManualMealSelection;
import com.nutrigain.nutrition.domain.model.aggregates.NutritionPlan;
import com.nutrigain.nutrition.domain.model.commands.AddMealToManualSelectionCommand;
import com.nutrigain.nutrition.domain.model.commands.GenerateNutritionPlanCommand;
import com.nutrigain.nutrition.domain.model.commands.RemoveMealFromManualSelectionCommand;
import com.nutrigain.nutrition.domain.model.valueobjects.NutritionFocus;
import com.nutrigain.nutrition.domain.services.NutritionCommandService;
import com.nutrigain.nutrition.domain.services.NutritionPlanGenerator;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.ManualMealSelectionRepository;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.MealRepository;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.NutritionPlanRepository;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NutritionCommandServiceImpl implements NutritionCommandService {

    private final MealRepository mealRepository;
    private final NutritionPlanRepository planRepository;
    private final ManualMealSelectionRepository selectionRepository;
    private final ExternalFitnessService externalFitnessService;

    public NutritionCommandServiceImpl(MealRepository mealRepository, NutritionPlanRepository planRepository,
                                       ManualMealSelectionRepository selectionRepository,
                                       ExternalFitnessService externalFitnessService) {
        this.mealRepository = mealRepository;
        this.planRepository = planRepository;
        this.selectionRepository = selectionRepository;
        this.externalFitnessService = externalFitnessService;
    }

    /** Creates (or replaces) the automatic plan of a user. */
    @Override
    @Transactional
    public NutritionPlan handle(GenerateNutritionPlanCommand command) {
        NutritionFocus focus = externalFitnessService.fetchFocusForPath(command.fitnessPathId());
        var entries = NutritionPlanGenerator.generate(focus, mealRepository.findAll());
        planRepository.findByUserId(command.userId()).ifPresent(existing -> {
            planRepository.delete(existing);
            planRepository.flush();
        });
        return planRepository.save(new NutritionPlan(command.userId(), focus, entries));
    }

    @Override
    @Transactional
    public ManualMealSelection handle(AddMealToManualSelectionCommand command) {
        if (!mealRepository.existsById(command.mealId())) {
            throw new ResourceNotFoundException("Meal", command.mealId());
        }
        ManualMealSelection selection = selectionRepository.findByUserId(command.userId())
                .orElseGet(() -> new ManualMealSelection(command.userId()));
        selection.add(command.mealId());
        return selectionRepository.save(selection);
    }

    @Override
    @Transactional
    public ManualMealSelection handle(RemoveMealFromManualSelectionCommand command) {
        ManualMealSelection selection = selectionRepository.findByUserId(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("ManualMealSelection", command.userId()));
        selection.remove(command.mealId());
        return selectionRepository.save(selection);
    }
}
