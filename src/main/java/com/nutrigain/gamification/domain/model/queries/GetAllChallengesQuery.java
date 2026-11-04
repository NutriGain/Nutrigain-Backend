package com.nutrigain.gamification.domain.model.queries;

import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;

/** category may be null to list every challenge. */
public record GetAllChallengesQuery(ChallengeCategory category) {
}
