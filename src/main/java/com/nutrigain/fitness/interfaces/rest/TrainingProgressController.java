package com.nutrigain.fitness.interfaces.rest;

import com.nutrigain.fitness.domain.model.commands.RegisterBodyWeightCommand;
import com.nutrigain.fitness.domain.model.commands.SetTargetWeightCommand;
import com.nutrigain.fitness.domain.model.queries.GetBodyWeightHistoryQuery;
import com.nutrigain.fitness.domain.model.queries.GetCurrentWeekProgressQuery;
import com.nutrigain.fitness.domain.model.queries.GetWeeklyProgressHistoryQuery;
import com.nutrigain.fitness.domain.services.TrainingProgressCommandService;
import com.nutrigain.fitness.domain.services.TrainingProgressQueryService;
import com.nutrigain.fitness.interfaces.rest.resources.BodyWeightResource;
import com.nutrigain.fitness.interfaces.rest.resources.RegisterBodyWeightResource;
import com.nutrigain.fitness.interfaces.rest.resources.SetTargetWeightResource;
import com.nutrigain.fitness.interfaces.rest.resources.WeeklyProgressResource;
import com.nutrigain.fitness.interfaces.rest.transform.FitnessResourceAssembler;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
@Tag(name = "Training progress")
public class TrainingProgressController {

    private final TrainingProgressQueryService queryService;
    private final TrainingProgressCommandService commandService;

    public TrainingProgressController(TrainingProgressQueryService queryService,
                                      TrainingProgressCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping("/progress")
    public List<WeeklyProgressResource> history(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetWeeklyProgressHistoryQuery(user.userId())).stream()
                .map(FitnessResourceAssembler::toResource).toList();
    }

    @GetMapping("/progress/current")
    public WeeklyProgressResource current(@AuthenticationPrincipal AuthenticatedUser user) {
        return FitnessResourceAssembler.toResource(queryService.handle(new GetCurrentWeekProgressQuery(user.userId())));
    }

    @PutMapping("/progress/target-weight")
    public WeeklyProgressResource setTargetWeight(@AuthenticationPrincipal AuthenticatedUser user,
                                                  @Valid @RequestBody SetTargetWeightResource body) {
        var progress = commandService.handle(new SetTargetWeightCommand(user.userId(), body.targetWeightKg()));
        return FitnessResourceAssembler.toResource(progress);
    }

    @GetMapping("/body-weights")
    public List<BodyWeightResource> bodyWeights(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetBodyWeightHistoryQuery(user.userId())).stream()
                .map(FitnessResourceAssembler::toResource).toList();
    }

    @PostMapping("/body-weights")
    @ResponseStatus(HttpStatus.CREATED)
    public BodyWeightResource registerBodyWeight(@AuthenticationPrincipal AuthenticatedUser user,
                                                 @Valid @RequestBody RegisterBodyWeightResource body) {
        var entry = commandService.handle(new RegisterBodyWeightCommand(user.userId(), body.weightKg(), body.recordedOn()));
        return FitnessResourceAssembler.toResource(entry);
    }
}
