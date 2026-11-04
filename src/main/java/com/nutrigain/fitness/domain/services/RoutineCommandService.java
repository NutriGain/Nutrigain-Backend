package com.nutrigain.fitness.domain.services;

import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import com.nutrigain.fitness.domain.model.commands.GenerateWeeklyRoutineCommand;

import java.util.List;

public interface RoutineCommandService {
    List<RoutineDay> handle(GenerateWeeklyRoutineCommand command);
}
