package com.nutrigain.achievements.domain.services;

import com.nutrigain.achievements.domain.model.commands.RecordActivityStatsCommand;
import com.nutrigain.achievements.domain.model.commands.RecordChallengeCommand;
import com.nutrigain.achievements.domain.model.commands.RecordWorkoutCommand;

public interface AchievementCommandService {
    void handle(RecordWorkoutCommand command);

    void handle(RecordChallengeCommand command);

    void handle(RecordActivityStatsCommand command);
}
