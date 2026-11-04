package com.nutrigain.social.interfaces.rest;

import com.nutrigain.social.domain.model.aggregates.Competitor;
import com.nutrigain.social.domain.model.commands.FollowUserCommand;
import com.nutrigain.social.domain.model.commands.UnfollowUserCommand;
import com.nutrigain.social.domain.model.queries.GetFollowingIdsQuery;
import com.nutrigain.social.domain.model.queries.GetFriendsLeaderboardQuery;
import com.nutrigain.social.domain.model.queries.GetGlobalLeaderboardQuery;
import com.nutrigain.social.domain.model.queries.SearchCompetitorsQuery;
import com.nutrigain.social.domain.services.SocialCommandService;
import com.nutrigain.social.domain.services.SocialQueryService;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Social")
public class SocialController {

    /** rank is the position inside the list (1-based) and is null in search results. */
    public record CompetitorResource(Integer rank, Long userId, String displayName, int level, int experience,
                                     int currentStreak, boolean following, boolean me) {
    }

    private final SocialQueryService queryService;
    private final SocialCommandService commandService;

    public SocialController(SocialQueryService queryService, SocialCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping("/social/leaderboard")
    public List<CompetitorResource> globalLeaderboard(@AuthenticationPrincipal AuthenticatedUser user,
                                                      @RequestParam(defaultValue = "20") int limit) {
        return toResources(user, queryService.handle(new GetGlobalLeaderboardQuery(limit)), true);
    }

    @GetMapping("/me/social/leaderboard")
    public List<CompetitorResource> friendsLeaderboard(@AuthenticationPrincipal AuthenticatedUser user) {
        return toResources(user, queryService.handle(new GetFriendsLeaderboardQuery(user.userId())), true);
    }

    @GetMapping("/social/users")
    public List<CompetitorResource> search(@AuthenticationPrincipal AuthenticatedUser user,
                                           @RequestParam String name) {
        return toResources(user, queryService.handle(new SearchCompetitorsQuery(name)), false);
    }

    @PutMapping("/me/following/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void follow(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long userId) {
        commandService.handle(new FollowUserCommand(user.userId(), userId));
    }

    @DeleteMapping("/me/following/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable Long userId) {
        commandService.handle(new UnfollowUserCommand(user.userId(), userId));
    }

    private List<CompetitorResource> toResources(AuthenticatedUser user, List<Competitor> competitors, boolean ranked) {
        Set<Long> following = queryService.handle(new GetFollowingIdsQuery(user.userId()));
        List<CompetitorResource> result = new ArrayList<>();
        for (int i = 0; i < competitors.size(); i++) {
            Competitor c = competitors.get(i);
            result.add(new CompetitorResource(ranked ? i + 1 : null, c.getUserId(), c.getDisplayName(), c.getLevel(),
                    c.getExperience(), c.getCurrentStreak(), following.contains(c.getUserId()),
                    c.getUserId().equals(user.userId())));
        }
        return result;
    }
}
