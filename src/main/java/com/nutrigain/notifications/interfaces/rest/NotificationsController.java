package com.nutrigain.notifications.interfaces.rest;

import com.nutrigain.notifications.domain.model.aggregates.Notification;
import com.nutrigain.notifications.domain.model.commands.MarkAllNotificationsSeenCommand;
import com.nutrigain.notifications.domain.model.commands.MarkNotificationSeenCommand;
import com.nutrigain.notifications.domain.model.queries.CountUnseenNotificationsQuery;
import com.nutrigain.notifications.domain.model.queries.GetNotificationsQuery;
import com.nutrigain.notifications.domain.model.valueobjects.NotificationType;
import com.nutrigain.notifications.domain.services.NotificationCommandService;
import com.nutrigain.notifications.domain.services.NotificationQueryService;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/me/notifications")
@Tag(name = "Notifications")
public class NotificationsController {

    public record NotificationResource(Long id, NotificationType type, String title, String message, boolean seen,
                                       Instant createdAt) {
    }

    private final NotificationQueryService queryService;
    private final NotificationCommandService commandService;

    public NotificationsController(NotificationQueryService queryService, NotificationCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping
    public List<NotificationResource> list(@AuthenticationPrincipal AuthenticatedUser user,
                                           @RequestParam(defaultValue = "false") boolean unseenOnly) {
        return queryService.handle(new GetNotificationsQuery(user.userId(), unseenOnly)).stream()
                .map(this::toResource).toList();
    }

    @GetMapping("/unseen-count")
    public Map<String, Long> unseenCount(@AuthenticationPrincipal AuthenticatedUser user) {
        return Map.of("count", queryService.handle(new CountUnseenNotificationsQuery(user.userId())));
    }

    @PostMapping("/{id}/seen")
    public NotificationResource markSeen(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long id) {
        return toResource(commandService.handle(new MarkNotificationSeenCommand(user.userId(), id)));
    }

    @PostMapping("/seen-all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAllSeen(@AuthenticationPrincipal AuthenticatedUser user) {
        commandService.handle(new MarkAllNotificationsSeenCommand(user.userId()));
    }

    private NotificationResource toResource(Notification n) {
        return new NotificationResource(n.getId(), n.getType(), n.getTitle(), n.getMessage(), n.isSeen(), n.getCreatedAt());
    }
}
