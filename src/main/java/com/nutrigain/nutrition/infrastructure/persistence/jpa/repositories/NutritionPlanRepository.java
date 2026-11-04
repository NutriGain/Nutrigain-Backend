package com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories;

import com.nutrigain.nutrition.domain.model.aggregates.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    Optional<NutritionPlan> findByUserId(Long userId);
}
