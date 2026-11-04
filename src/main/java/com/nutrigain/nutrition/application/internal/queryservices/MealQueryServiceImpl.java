package com.nutrigain.nutrition.application.internal.queryservices;

import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.queries.GetAllMealsQuery;
import com.nutrigain.nutrition.domain.model.queries.GetMealByIdQuery;
import com.nutrigain.nutrition.domain.model.queries.GetMealsByIdsQuery;
import com.nutrigain.nutrition.domain.services.MealQueryService;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.MealRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MealQueryServiceImpl implements MealQueryService {

    private final MealRepository mealRepository;

    public MealQueryServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public List<Meal> handle(GetAllMealsQuery query) {
        return query.mealType() == null ? mealRepository.findAll() : mealRepository.findByMealType(query.mealType());
    }

    @Override
    public Optional<Meal> handle(GetMealByIdQuery query) {
        return mealRepository.findById(query.mealId());
    }

    @Override
    public List<Meal> handle(GetMealsByIdsQuery query) {
        return mealRepository.findAllById(query.mealIds());
    }
}
