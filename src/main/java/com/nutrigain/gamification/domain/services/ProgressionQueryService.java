package com.nutrigain.gamification.domain.services;

import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import com.nutrigain.gamification.domain.model.aggregates.MotivationalMessage;
import com.nutrigain.gamification.domain.model.aggregates.Streak;
import com.nutrigain.gamification.domain.model.queries.GetAvatarByUserIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetRandomMotivationalMessageQuery;
import com.nutrigain.gamification.domain.model.queries.GetStreakByUserIdQuery;

import java.util.Optional;

public interface ProgressionQueryService {
    Optional<Avatar> handle(GetAvatarByUserIdQuery query);

    Optional<Streak> handle(GetStreakByUserIdQuery query);

    Optional<MotivationalMessage> handle(GetRandomMotivationalMessageQuery query);
}
