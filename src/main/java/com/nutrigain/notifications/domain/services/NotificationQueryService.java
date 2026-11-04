package com.nutrigain.notifications.domain.services;

import com.nutrigain.notifications.domain.model.aggregates.Notification;
import com.nutrigain.notifications.domain.model.queries.CountUnseenNotificationsQuery;
import com.nutrigain.notifications.domain.model.queries.GetNotificationsQuery;

import java.util.List;

public interface NotificationQueryService {
    List<Notification> handle(GetNotificationsQuery query);

    long handle(CountUnseenNotificationsQuery query);
}
