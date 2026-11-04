package com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories;

import com.nutrigain.nutrition.domain.model.aggregates.ManualMealSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManualMealSelectionRepository extends JpaRepository<ManualMealSelection, Long> {
    Optional<ManualMealSelection> findByUserId(Long userId);
}
