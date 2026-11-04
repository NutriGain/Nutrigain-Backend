package com.nutrigain.notifications.application.internal.queryservices;

import com.nutrigain.notifications.domain.model.aggregates.Notification;
import com.nutrigain.notifications.domain.model.queries.CountUnseenNotificationsQuery;
import com.nutrigain.notifications.domain.model.queries.GetNotificationsQuery;
import com.nutrigain.notifications.domain.services.NotificationQueryService;
import com.nutrigain.notifications.infrastructure.persistence.jpa.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository repository;

    public NotificationQueryServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Notification> handle(GetNotificationsQuery query) {
        return query.unseenOnly()
                ? repository.findTop50ByUserIdAndSeenFalseOrderByIdDesc(query.userId())
                : repository.findTop50ByUserIdOrderByIdDesc(query.userId());
    }

    @Override
    public long handle(CountUnseenNotificationsQuery query) {
        return repository.countByUserIdAndSeenFalse(query.userId());
    }
}
