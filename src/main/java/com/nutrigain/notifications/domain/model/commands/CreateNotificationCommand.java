package com.nutrigain.notifications.domain.model.commands;

import com.nutrigain.notifications.domain.model.valueobjects.NotificationType;

public record CreateNotificationCommand(Long userId, NotificationType type, String title, String message) {
}
