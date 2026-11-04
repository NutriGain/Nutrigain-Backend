package com.nutrigain.nutrition.interfaces.rest;

import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.commands.AddMealToManualSelectionCommand;
import com.nutrigain.nutrition.domain.model.commands.RemoveMealFromManualSelectionCommand;
import com.nutrigain.nutrition.domain.model.queries.GetManualSelectionByUserIdQuery;
import com.nutrigain.nutrition.domain.model.queries.GetMealsByIdsQuery;
import com.nutrigain.nutrition.domain.model.queries.GetNutritionPlanByUserIdQuery;
import com.nutrigain.nutrition.domain.services.MealQueryService;
import com.nutrigain.nutrition.domain.services.NutritionCommandService;
import com.nutrigain.nutrition.domain.services.NutritionQueryService;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.ManualPlanResource;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.NutritionPlanResource;
import com.nutrigain.nutrition.interfaces.rest.transform.NutritionResourceAssembler;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/me")
@Tag(name = "Nutrition plans")
public class NutritionPlanController {

    private final NutritionQueryService queryService;
    private final NutritionCommandService commandService;
    private final MealQueryService mealQueryService;

    public NutritionPlanController(NutritionQueryService queryService, NutritionCommandService commandService,
                                   MealQueryService mealQueryService) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.mealQueryService = mealQueryService;
    }

    @GetMapping("/nutrition-plan")
    public NutritionPlanResource automaticPlan(@AuthenticationPrincipal AuthenticatedUser user) {
        var plan = queryService.handle(new GetNutritionPlanByUserIdQuery(user.userId()))
                .orElseThrow(() -> new ResourceNotFoundException("NutritionPlan", user.userId()));
        List<Long> ids = plan.getEntries().stream().map(e -> e.mealId()).distinct().toList();
        return NutritionResourceAssembler.toResource(plan, mealQueryService.handle(new GetMealsByIdsQuery(ids)));
    }

    @GetMapping("/manual-plan")
    public ManualPlanResource manualPlan(@AuthenticationPrincipal AuthenticatedUser user) {
        Set<Long> ids = queryService.handle(new GetManualSelectionByUserIdQuery(user.userId()))
                .map(s -> s.getMealIds()).orElse(Set.of());
        return toManualPlan(ids);
    }

    @PutMapping("/manual-plan/meals/{mealId}")
    public ManualPlanResource addMeal(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long mealId) {
        var selection = commandService.handle(new AddMealToManualSelectionCommand(user.userId(), mealId));
        return toManualPlan(selection.getMealIds());
    }

    @DeleteMapping("/manual-plan/meals/{mealId}")
    public ManualPlanResource removeMeal(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long mealId) {
        var selection = commandService.handle(new RemoveMealFromManualSelectionCommand(user.userId(), mealId));
        return toManualPlan(selection.getMealIds());
    }

    private ManualPlanResource toManualPlan(Set<Long> ids) {
        List<Meal> meals = ids.isEmpty() ? List.of() : mealQueryService.handle(new GetMealsByIdsQuery(ids));
        return NutritionResourceAssembler.toManualPlan(meals);
    }
}
