package com.nutrigain.fitness.application.internal.eventhandlers;

import com.nutrigain.fitness.domain.model.commands.GenerateWeeklyRoutineCommand;
import com.nutrigain.fitness.domain.services.RoutineCommandService;
import com.nutrigain.iam.domain.model.events.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** When a user signs up, Fitness builds their first weekly routine from the chosen path. */
@Component("fitnessUserRegisteredEventHandler")
public class UserRegisteredEventHandler {

    private final RoutineCommandService routineCommandService;

    public UserRegisteredEventHandler(RoutineCommandService routineCommandService) {
        this.routineCommandService = routineCommandService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {
        routineCommandService.handle(new GenerateWeeklyRoutineCommand(event.userId(), event.fitnessPathId()));
    }
}
