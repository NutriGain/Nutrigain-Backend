package com.nutrigain.notifications.application.internal.commandservices;

import com.nutrigain.notifications.domain.model.aggregates.Notification;
import com.nutrigain.notifications.domain.model.commands.CreateNotificationCommand;
import com.nutrigain.notifications.domain.model.commands.MarkAllNotificationsSeenCommand;
import com.nutrigain.notifications.domain.model.commands.MarkNotificationSeenCommand;
import com.nutrigain.notifications.domain.services.NotificationCommandService;
import com.nutrigain.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository repository;

    public NotificationCommandServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Notification handle(CreateNotificationCommand command) {
        return repository.save(new Notification(command.userId(), command.type(), command.title(), command.message()));
    }

    @Override
    @Transactional
    public Notification handle(MarkNotificationSeenCommand command) {
        Notification notification = repository.findByIdAndUserId(command.notificationId(), command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification", command.notificationId()));
        notification.markSeen();
        return repository.save(notification);
    }

    @Override
    @Transactional
    public void handle(MarkAllNotificationsSeenCommand command) {
        repository.markAllSeen(command.userId());
    }
}
