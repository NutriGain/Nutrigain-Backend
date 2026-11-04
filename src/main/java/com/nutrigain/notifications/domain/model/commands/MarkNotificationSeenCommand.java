package com.nutrigain.notifications.domain.model.commands;

public record MarkNotificationSeenCommand(Long userId, Long notificationId) {
}
