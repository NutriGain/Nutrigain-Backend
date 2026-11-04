package com.nutrigain.nutrition.domain.model.queries;

import java.util.Collection;

public record GetMealsByIdsQuery(Collection<Long> mealIds) {
}
