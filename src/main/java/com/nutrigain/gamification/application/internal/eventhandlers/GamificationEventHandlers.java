package com.nutrigain.gamification.application.internal.eventhandlers;

import com.nutrigain.fitness.domain.model.events.WorkoutCompletedEvent;
import com.nutrigain.gamification.domain.model.commands.CreateAvatarCommand;
import com.nutrigain.gamification.domain.model.commands.InitializeStreakCommand;
import com.nutrigain.gamification.domain.model.commands.RecordActivityCommand;
import com.nutrigain.gamification.domain.model.events.ChallengeCompletedEvent;
import com.nutrigain.gamification.domain.services.ProgressionCommandService;
import com.nutrigain.iam.domain.model.events.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Reacts to what happens in other contexts (and in Gamification itself) to keep avatar and streak up to date. */
@Component
public class GamificationEventHandlers {

    /** Experience earned for finishing a workout. */
    public static final int WORKOUT_EXPERIENCE = 20;

    private final ProgressionCommandService progressionCommandService;

    public GamificationEventHandlers(ProgressionCommandService progressionCommandService) {
        this.progressionCommandService = progressionCommandService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {
        progressionCommandService.handle(new CreateAvatarCommand(event.userId(), event.fitnessPathId()));
        progressionCommandService.handle(new InitializeStreakCommand(event.userId()));
    }

    @EventListener
    public void on(WorkoutCompletedEvent event) {
        progressionCommandService.handle(
                new RecordActivityCommand(event.userId(), WORKOUT_EXPERIENCE, event.completedOn()));
    }

    @EventListener
    public void on(ChallengeCompletedEvent event) {
        progressionCommandService.handle(
                new RecordActivityCommand(event.userId(), event.xpAwarded(), event.completedOn()));
    }
}
