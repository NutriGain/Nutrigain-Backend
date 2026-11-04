package com.nutrigain.fitness.infrastructure.persistence.jpa.repositories;

import com.nutrigain.fitness.domain.model.aggregates.FitnessPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessPathRepository extends JpaRepository<FitnessPath, Long> {
}
