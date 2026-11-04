package com.nutrigain.gamification.infrastructure.persistence.jpa.repositories;

import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentKind;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeAssignmentRepository extends JpaRepository<ChallengeAssignment, Long> {
    List<ChallengeAssignment> findByUserIdOrderByIdDesc(Long userId);

    Optional<ChallengeAssignment> findByIdAndUserId(Long id, Long userId);

    Optional<ChallengeAssignment> findByUserIdAndKindAndAssignedOn(Long userId, AssignmentKind kind, LocalDate assignedOn);

    long countByUserIdAndKindAndStatus(Long userId, AssignmentKind kind, AssignmentStatus status);
}
