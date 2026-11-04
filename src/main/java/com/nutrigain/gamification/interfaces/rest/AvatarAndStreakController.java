package com.nutrigain.gamification.interfaces.rest;

import com.nutrigain.gamification.domain.model.queries.GetAvatarByUserIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetRandomMotivationalMessageQuery;
import com.nutrigain.gamification.domain.model.queries.GetStreakByUserIdQuery;
import com.nutrigain.gamification.domain.services.ProgressionQueryService;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.AvatarResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.MotivationalMessageResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.StreakResource;
import com.nutrigain.gamification.interfaces.rest.transform.GamificationResourceAssembler;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Avatar and streak")
public class AvatarAndStreakController {

    private final ProgressionQueryService queryService;
    private final Clock clock;

    public AvatarAndStreakController(ProgressionQueryService queryService, Clock clock) {
        this.queryService = queryService;
        this.clock = clock;
    }

    @GetMapping("/me/avatar")
    public AvatarResource avatar(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetAvatarByUserIdQuery(user.userId()))
                .map(GamificationResourceAssembler::toResource)
                .orElseThrow(() -> new ResourceNotFoundException("Avatar", user.userId()));
    }

    @GetMapping("/me/streak")
    public StreakResource streak(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetStreakByUserIdQuery(user.userId()))
                .map(s -> GamificationResourceAssembler.toResource(s, LocalDate.now(clock)))
                .orElseThrow(() -> new ResourceNotFoundException("Streak", user.userId()));
    }

    @GetMapping("/motivational-messages/random")
    public MotivationalMessageResource randomMessage() {
        return queryService.handle(new GetRandomMotivationalMessageQuery())
                .map(GamificationResourceAssembler::toResource)
                .orElseThrow(() -> new ResourceNotFoundException("MotivationalMessage", "any"));
    }
}
