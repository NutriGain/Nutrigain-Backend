package com.nutrigain.gamification.infrastructure.persistence.jpa.repositories;

import com.nutrigain.gamification.domain.model.aggregates.MotivationalMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivationalMessageRepository extends JpaRepository<MotivationalMessage, Long> {
}
