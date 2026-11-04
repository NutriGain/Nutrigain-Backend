package com.nutrigain.gamification.infrastructure.persistence.jpa.repositories;

import com.nutrigain.gamification.domain.model.aggregates.Challenge;
import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;
import com.nutrigain.gamification.domain.model.valueobjects.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByCategory(ChallengeCategory category);

    List<Challenge> findByDifficultyOrderByIdAsc(Difficulty difficulty);

    List<Challenge> findByDifficultyNot(Difficulty difficulty);

    List<Challenge> findByDifficultyNotAndCategory(Difficulty difficulty, ChallengeCategory category);
}
