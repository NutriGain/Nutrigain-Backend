package com.nutrigain.notifications.domain.services;

import com.nutrigain.notifications.domain.model.aggregates.Notification;
import com.nutrigain.notifications.domain.model.commands.CreateNotificationCommand;
import com.nutrigain.notifications.domain.model.commands.MarkAllNotificationsSeenCommand;
import com.nutrigain.notifications.domain.model.commands.MarkNotificationSeenCommand;

public interface NotificationCommandService {
    Notification handle(CreateNotificationCommand command);

    Notification handle(MarkNotificationSeenCommand command);

    void handle(MarkAllNotificationsSeenCommand command);
}
