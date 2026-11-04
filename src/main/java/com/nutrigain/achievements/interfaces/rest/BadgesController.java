package com.nutrigain.achievements.interfaces.rest;

import com.nutrigain.achievements.domain.model.aggregates.UserBadge;
import com.nutrigain.achievements.domain.model.queries.GetBadgesByUserIdQuery;
import com.nutrigain.achievements.domain.model.valueobjects.BadgeType;
import com.nutrigain.achievements.domain.services.AchievementQueryService;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/me/badges")
@Tag(name = "Achievements")
public class BadgesController {

    public record BadgeResource(String code, String title, String description, boolean unlocked, LocalDate awardedOn) {
    }

    private final AchievementQueryService queryService;

    public BadgesController(AchievementQueryService queryService) {
        this.queryService = queryService;
    }

    /** Every badge of the game, marking which ones the user already unlocked. */
    @GetMapping
    public List<BadgeResource> myBadges(@AuthenticationPrincipal AuthenticatedUser user) {
        Map<BadgeType, UserBadge> owned = queryService.handle(new GetBadgesByUserIdQuery(user.userId())).stream()
                .collect(Collectors.toMap(UserBadge::getBadge, Function.identity()));
        return Arrays.stream(BadgeType.values())
                .map(b -> new BadgeResource(b.name(), b.title(), b.description(), owned.containsKey(b),
                        owned.containsKey(b) ? owned.get(b).getAwardedOn() : null))
                .toList();
    }
}
