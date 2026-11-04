package com.nutrigain.gamification.domain.services;

import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import com.nutrigain.gamification.domain.model.aggregates.Streak;
import com.nutrigain.gamification.domain.model.commands.CreateAvatarCommand;
import com.nutrigain.gamification.domain.model.commands.InitializeStreakCommand;
import com.nutrigain.gamification.domain.model.commands.RecordActivityCommand;

public interface ProgressionCommandService {
    Avatar handle(CreateAvatarCommand command);

    Streak handle(InitializeStreakCommand command);

    /** Grants experience to the avatar and registers the activity in the streak. */
    void handle(RecordActivityCommand command);
}
