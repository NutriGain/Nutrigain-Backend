package com.nutrigain.nutrition.domain.services;

import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.queries.GetAllMealsQuery;
import com.nutrigain.nutrition.domain.model.queries.GetMealByIdQuery;
import com.nutrigain.nutrition.domain.model.queries.GetMealsByIdsQuery;

import java.util.List;
import java.util.Optional;

public interface MealQueryService {
    List<Meal> handle(GetAllMealsQuery query);

    Optional<Meal> handle(GetMealByIdQuery query);

    List<Meal> handle(GetMealsByIdsQuery query);
}
