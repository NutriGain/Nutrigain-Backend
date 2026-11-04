package com.nutrigain.notifications.application.internal.eventhandlers;

import com.nutrigain.achievements.domain.model.events.BadgeAwardedEvent;
import com.nutrigain.gamification.domain.model.events.ActivityRecordedEvent;
import com.nutrigain.gamification.domain.model.events.ChallengeCompletedEvent;
import com.nutrigain.iam.domain.model.events.UserRegisteredEvent;
import com.nutrigain.notifications.domain.model.commands.CreateNotificationCommand;
import com.nutrigain.notifications.domain.model.valueobjects.NotificationType;
import com.nutrigain.notifications.domain.services.NotificationCommandService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** Turns what happens in other contexts into inbox messages. */
@Component
public class NotificationEventHandlers {

    private final NotificationCommandService commandService;

    public NotificationEventHandlers(NotificationCommandService commandService) {
        this.commandService = commandService;
    }

    @EventListener
    public void on(UserRegisteredEvent event) {
        commandService.handle(new CreateNotificationCommand(event.userId(), NotificationType.WELCOME,
                "Bienvenido a NutriGain", "Hola " + event.name() + ", tu rutina y tu plan nutricional ya estan listos."));
    }

    @EventListener
    public void on(BadgeAwardedEvent event) {
        commandService.handle(new CreateNotificationCommand(event.userId(), NotificationType.BADGE_AWARDED,
                "Nueva insignia", "Desbloqueaste: " + event.badgeTitle()));
    }

    @EventListener
    public void on(ChallengeCompletedEvent event) {
        commandService.handle(new CreateNotificationCommand(event.userId(), NotificationType.CHALLENGE_COMPLETED,
                "Reto completado", "Ganaste " + event.xpAwarded() + " XP."));
    }

    @EventListener
    public void on(ActivityRecordedEvent event) {
        if (event.leveledUp()) {
            commandService.handle(new CreateNotificationCommand(event.userId(), NotificationType.LEVEL_UP,
                    "Subiste de nivel", "Tu personaje alcanzo el nivel " + event.level() + "."));
        }
    }
}
