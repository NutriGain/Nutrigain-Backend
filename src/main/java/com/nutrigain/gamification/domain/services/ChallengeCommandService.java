package com.nutrigain.gamification.domain.services;

import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.commands.AssignDailyExtremeChallengeCommand;
import com.nutrigain.gamification.domain.model.commands.CompleteChallengeCommand;
import com.nutrigain.gamification.domain.model.commands.DrawRandomChallengeCommand;

public interface ChallengeCommandService {
    ChallengeAssignment handle(DrawRandomChallengeCommand command);

    /** Idempotent: returns the extreme challenge of the day, creating it the first time. */
    ChallengeAssignment handle(AssignDailyExtremeChallengeCommand command);

    ChallengeAssignment handle(CompleteChallengeCommand command);
}
