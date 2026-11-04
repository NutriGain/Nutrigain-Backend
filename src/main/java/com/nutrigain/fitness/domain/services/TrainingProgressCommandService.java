package com.nutrigain.fitness.domain.services;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import com.nutrigain.fitness.domain.model.commands.CompleteWorkoutCommand;
import com.nutrigain.fitness.domain.model.commands.RegisterBodyWeightCommand;
import com.nutrigain.fitness.domain.model.commands.SetTargetWeightCommand;

public interface TrainingProgressCommandService {
    WeeklyProgress handle(CompleteWorkoutCommand command);

    WeeklyProgress handle(SetTargetWeightCommand command);

    BodyWeightEntry handle(RegisterBodyWeightCommand command);
}
