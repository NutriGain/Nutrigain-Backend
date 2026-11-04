package com.nutrigain.notifications.domain.model.queries;

public record GetNotificationsQuery(Long userId, boolean unseenOnly) {
}
