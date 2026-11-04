package com.nutrigain.fitness.infrastructure.persistence.jpa.repositories;

import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyProgressRepository extends JpaRepository<WeeklyProgress, Long> {
    Optional<WeeklyProgress> findByUserIdAndWeekStart(Long userId, LocalDate weekStart);

    Optional<WeeklyProgress> findFirstByUserIdOrderByWeekStartDesc(Long userId);

    List<WeeklyProgress> findByUserIdOrderByWeekStartAsc(Long userId);
}
