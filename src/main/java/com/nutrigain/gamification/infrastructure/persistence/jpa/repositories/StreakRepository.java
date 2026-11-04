package com.nutrigain.gamification.infrastructure.persistence.jpa.repositories;

import com.nutrigain.gamification.domain.model.aggregates.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {
    Optional<Streak> findByUserId(Long userId);
}
