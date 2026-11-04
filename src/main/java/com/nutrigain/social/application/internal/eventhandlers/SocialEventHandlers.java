package com.nutrigain.social.application.internal.eventhandlers;

import com.nutrigain.gamification.domain.model.events.ActivityRecordedEvent;
import com.nutrigain.iam.domain.model.events.UserRegisteredEvent;
import com.nutrigain.social.domain.model.commands.RegisterCompetitorCommand;
import com.nutrigain.social.domain.model.commands.UpdateCompetitorScoreCommand;
import com.nutrigain.social.domain.services.SocialCommandService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Keeps the social read model in sync with IAM and Gamification. */
@Component
public class SocialEventHandlers {

    private final SocialCommandService commandService;

    public SocialEventHandlers(SocialCommandService commandService) {
        this.commandService = commandService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {
        commandService.handle(new RegisterCompetitorCommand(event.userId(), event.name()));
    }

    @EventListener
    public void on(ActivityRecordedEvent event) {
        commandService.handle(new UpdateCompetitorScoreCommand(event.userId(), event.totalExperience(),
                event.level(), event.currentStreak()));
    }
}
