package com.nutrigain.achievements.application.internal.eventhandlers;

import com.nutrigain.achievements.domain.model.commands.RecordActivityStatsCommand;
import com.nutrigain.achievements.domain.model.commands.RecordChallengeCommand;
import com.nutrigain.achievements.domain.model.commands.RecordWorkoutCommand;
import com.nutrigain.achievements.domain.services.AchievementCommandService;
import com.nutrigain.fitness.domain.model.events.WorkoutCompletedEvent;
import com.nutrigain.gamification.domain.model.events.ActivityRecordedEvent;
import com.nutrigain.gamification.domain.model.events.ChallengeCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AchievementEventHandlers {

    private final AchievementCommandService commandService;

    public AchievementEventHandlers(AchievementCommandService commandService) {
        this.commandService = commandService;
    }

    @EventListener
    public void on(WorkoutCompletedEvent event) {
        commandService.handle(new RecordWorkoutCommand(event.userId()));
    }

    @EventListener
    public void on(ChallengeCompletedEvent event) {
        commandService.handle(new RecordChallengeCommand(event.userId(), event.extreme()));
    }

    @EventListener
    public void on(ActivityRecordedEvent event) {
        commandService.handle(new RecordActivityStatsCommand(event.userId(), event.currentStreak(), event.level()));
    }
}
