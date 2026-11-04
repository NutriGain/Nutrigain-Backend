package com.nutrigain.fitness.infrastructure.persistence.jpa.repositories;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BodyWeightEntryRepository extends JpaRepository<BodyWeightEntry, Long> {
    Optional<BodyWeightEntry> findByUserIdAndRecordedOn(Long userId, LocalDate recordedOn);

    List<BodyWeightEntry> findByUserIdOrderByRecordedOnAsc(Long userId);
}
