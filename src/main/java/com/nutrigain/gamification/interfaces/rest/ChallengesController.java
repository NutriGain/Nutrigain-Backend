package com.nutrigain.gamification.interfaces.rest;

import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.commands.AssignDailyExtremeChallengeCommand;
import com.nutrigain.gamification.domain.model.commands.CompleteChallengeCommand;
import com.nutrigain.gamification.domain.model.commands.DrawRandomChallengeCommand;
import com.nutrigain.gamification.domain.model.queries.GetAllChallengesQuery;
import com.nutrigain.gamification.domain.model.queries.GetAssignmentsByUserIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetChallengeByIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetTodaysDailyExtremeQuery;
import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;
import com.nutrigain.gamification.domain.services.ChallengeCommandService;
import com.nutrigain.gamification.domain.services.ChallengeQueryService;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.ChallengeAssignmentResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.ChallengeResource;
import com.nutrigain.gamification.interfaces.rest.transform.GamificationResourceAssembler;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Challenges")
public class ChallengesController {

    private final ChallengeQueryService queryService;
    private final ChallengeCommandService commandService;

    public ChallengesController(ChallengeQueryService queryService, ChallengeCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    /** The catalog of challenges, optionally filtered by category. */
    @GetMapping("/challenges")
    public List<ChallengeResource> catalog(@RequestParam(required = false) ChallengeCategory category) {
        return queryService.handle(new GetAllChallengesQuery(category)).stream()
                .map(GamificationResourceAssembler::toResource).toList();
    }

    @GetMapping("/me/challenges")
    public List<ChallengeAssignmentResource> myChallenges(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetAssignmentsByUserIdQuery(user.userId())).stream()
                .map(this::toResource).toList();
    }

    /** Draws a random challenge (never an extreme one) for the user. */
    @PostMapping("/me/challenges/draw")
    @ResponseStatus(HttpStatus.CREATED)
    public ChallengeAssignmentResource draw(@AuthenticationPrincipal AuthenticatedUser user,
                                            @RequestParam(required = false) ChallengeCategory category) {
        return toResource(commandService.handle(new DrawRandomChallengeCommand(user.userId(), category)));
    }

    @PostMapping("/me/challenges/{assignmentId}/complete")
    public ChallengeAssignmentResource complete(@AuthenticationPrincipal AuthenticatedUser user,
                                                @PathVariable Long assignmentId) {
        return toResource(commandService.handle(new CompleteChallengeCommand(user.userId(), assignmentId)));
    }

    /** Today's extreme challenge, 404 if the user has not asked for it yet. */
    @GetMapping("/me/challenges/daily-extreme")
    public ChallengeAssignmentResource dailyExtreme(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetTodaysDailyExtremeQuery(user.userId()))
                .map(this::toResource)
                .orElseThrow(() -> new ResourceNotFoundException("DailyExtremeChallenge", "today"));
    }

    /** Idempotent: gets today's extreme challenge, creating it if this is the first request of the day. */
    @PostMapping("/me/challenges/daily-extreme")
    public ChallengeAssignmentResource requestDailyExtreme(@AuthenticationPrincipal AuthenticatedUser user) {
        return toResource(commandService.handle(new AssignDailyExtremeChallengeCommand(user.userId())));
    }

    private ChallengeAssignmentResource toResource(ChallengeAssignment assignment) {
        var challenge = queryService.handle(new GetChallengeByIdQuery(assignment.getChallengeId()))
                .orElseThrow(() -> new ResourceNotFoundException("Challenge", assignment.getChallengeId()));
        return GamificationResourceAssembler.toResource(assignment, challenge);
    }
}
