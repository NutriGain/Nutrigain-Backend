package com.nutrigain.gamification.domain.services;

import com.nutrigain.gamification.domain.model.aggregates.Challenge;
import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.queries.GetAllChallengesQuery;
import com.nutrigain.gamification.domain.model.queries.GetAssignmentsByUserIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetChallengeByIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetTodaysDailyExtremeQuery;

import java.util.List;
import java.util.Optional;

public interface ChallengeQueryService {
    List<Challenge> handle(GetAllChallengesQuery query);

    Optional<Challenge> handle(GetChallengeByIdQuery query);

    List<ChallengeAssignment> handle(GetAssignmentsByUserIdQuery query);

    Optional<ChallengeAssignment> handle(GetTodaysDailyExtremeQuery query);
}
