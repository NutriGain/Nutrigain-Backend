package com.nutrigain.achievements.infrastructure.persistence.jpa.repositories;

import com.nutrigain.achievements.domain.model.aggregates.AchievementTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementTrackerRepository extends JpaRepository<AchievementTracker, Long> {
    Optional<AchievementTracker> findByUserId(Long userId);
}
