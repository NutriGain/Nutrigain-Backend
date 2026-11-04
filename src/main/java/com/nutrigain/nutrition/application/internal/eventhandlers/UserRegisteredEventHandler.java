package com.nutrigain.nutrition.application.internal.eventhandlers;

import com.nutrigain.iam.domain.model.events.UserRegisteredEvent;
import com.nutrigain.nutrition.domain.model.commands.GenerateNutritionPlanCommand;
import com.nutrigain.nutrition.domain.services.NutritionCommandService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** When a user signs up, Nutrition builds their first automatic plan. */
@Component("nutritionUserRegisteredEventHandler")
public class UserRegisteredEventHandler {

    private final NutritionCommandService commandService;

    public UserRegisteredEventHandler(NutritionCommandService commandService) {
        this.commandService = commandService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {
        commandService.handle(new GenerateNutritionPlanCommand(event.userId(), event.fitnessPathId()));
    }
}
