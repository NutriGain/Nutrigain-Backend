package com.nutrigain.nutrition.interfaces.rest;

import com.nutrigain.nutrition.domain.model.queries.GetAllMealsQuery;
import com.nutrigain.nutrition.domain.model.queries.GetMealByIdQuery;
import com.nutrigain.nutrition.domain.model.valueobjects.MealType;
import com.nutrigain.nutrition.domain.services.MealQueryService;
import com.nutrigain.nutrition.interfaces.rest.resources.NutritionResources.MealResource;
import com.nutrigain.nutrition.interfaces.rest.transform.NutritionResourceAssembler;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meals")
@Tag(name = "Meals")
public class MealsController {

    private final MealQueryService mealQueryService;

    public MealsController(MealQueryService mealQueryService) {
        this.mealQueryService = mealQueryService;
    }

    @GetMapping
    public List<MealResource> getAll(@RequestParam(required = false) MealType type) {
        return mealQueryService.handle(new GetAllMealsQuery(type)).stream()
                .map(NutritionResourceAssembler::toResource).toList();
    }

    @GetMapping("/{id}")
    public MealResource getById(@PathVariable Long id) {
        return mealQueryService.handle(new GetMealByIdQuery(id))
                .map(NutritionResourceAssembler::toResource)
                .orElseThrow(() -> new ResourceNotFoundException("Meal", id));
    }
}
