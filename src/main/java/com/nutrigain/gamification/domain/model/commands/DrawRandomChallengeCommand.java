package com.nutrigain.gamification.domain.model.commands;

import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;

/** category may be null to draw from any category. */
public record DrawRandomChallengeCommand(Long userId, ChallengeCategory category) {
}
