package com.nutrigain.fitness.infrastructure.persistence.jpa.repositories;

import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineDayRepository extends JpaRepository<RoutineDay, Long> {
    List<RoutineDay> findByUserId(Long userId);

    Optional<RoutineDay> findByIdAndUserId(Long id, Long userId);

    long countByUserId(Long userId);

    void deleteByUserId(Long userId);
}
