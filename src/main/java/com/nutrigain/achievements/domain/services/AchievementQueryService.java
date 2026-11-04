package com.nutrigain.achievements.domain.services;

import com.nutrigain.achievements.domain.model.aggregates.UserBadge;
import com.nutrigain.achievements.domain.model.queries.GetBadgesByUserIdQuery;

import java.util.List;

public interface AchievementQueryService {
    List<UserBadge> handle(GetBadgesByUserIdQuery query);
}
