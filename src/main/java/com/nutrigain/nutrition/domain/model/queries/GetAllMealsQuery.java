package com.nutrigain.nutrition.domain.model.queries;

import com.nutrigain.nutrition.domain.model.valueobjects.MealType;

/** mealType may be null to list every meal. */
public record GetAllMealsQuery(MealType mealType) {
}
