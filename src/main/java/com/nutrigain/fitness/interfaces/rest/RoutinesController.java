package com.nutrigain.fitness.interfaces.rest;

import com.nutrigain.fitness.domain.model.commands.CompleteWorkoutCommand;
import com.nutrigain.fitness.domain.model.queries.GetRoutinesByUserIdQuery;
import com.nutrigain.fitness.domain.services.RoutineQueryService;
import com.nutrigain.fitness.domain.services.TrainingProgressCommandService;
import com.nutrigain.fitness.interfaces.rest.resources.RoutineDayResource;
import com.nutrigain.fitness.interfaces.rest.resources.WeeklyProgressResource;
import com.nutrigain.fitness.interfaces.rest.transform.FitnessResourceAssembler;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me/routines")
@Tag(name = "Routines")
public class RoutinesController {

    private final RoutineQueryService routineQueryService;
    private final TrainingProgressCommandService progressCommandService;

    public RoutinesController(RoutineQueryService routineQueryService,
                              TrainingProgressCommandService progressCommandService) {
        this.routineQueryService = routineQueryService;
        this.progressCommandService = progressCommandService;
    }

    @GetMapping
    public List<RoutineDayResource> myRoutines(@AuthenticationPrincipal AuthenticatedUser user) {
        return routineQueryService.handle(new GetRoutinesByUserIdQuery(user.userId())).stream()
                .map(FitnessResourceAssembler::toResource).toList();
    }

    /** Marks a routine day as done for the current week and returns the updated weekly progress. */
    @PostMapping("/{routineDayId}/completions")
    @ResponseStatus(HttpStatus.CREATED)
    public WeeklyProgressResource complete(@AuthenticationPrincipal AuthenticatedUser user,
                                           @PathVariable Long routineDayId) {
        var progress = progressCommandService.handle(new CompleteWorkoutCommand(user.userId(), routineDayId));
        return FitnessResourceAssembler.toResource(progress);
    }
}
