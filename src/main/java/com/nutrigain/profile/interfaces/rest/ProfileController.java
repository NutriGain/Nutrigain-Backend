package com.nutrigain.profile.interfaces.rest;

import com.nutrigain.profile.domain.model.commands.UpdateProfileCommand;
import com.nutrigain.profile.domain.model.queries.GetHealthSummaryQuery;
import com.nutrigain.profile.domain.model.valueobjects.ActivityLevel;
import com.nutrigain.profile.domain.model.valueobjects.HealthSummary;
import com.nutrigain.profile.domain.model.valueobjects.Sex;
import com.nutrigain.profile.domain.services.ProfileCommandService;
import com.nutrigain.profile.domain.services.ProfileQueryService;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me/profile")
@Tag(name = "Profile")
public class ProfileController {

    public record UpdateProfileResource(@Min(100) @Max(250) int heightCm, @NotNull Sex sex,
                                        @NotNull ActivityLevel activityLevel) {
    }

    private final ProfileCommandService commandService;
    private final ProfileQueryService queryService;

    public ProfileController(ProfileCommandService commandService, ProfileQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /** Profile data plus BMI and daily calorie target (null while height or weight are unknown). */
    @GetMapping
    public HealthSummary get(@AuthenticationPrincipal AuthenticatedUser user) {
        return queryService.handle(new GetHealthSummaryQuery(user.userId()));
    }

    @PutMapping
    public HealthSummary update(@AuthenticationPrincipal AuthenticatedUser user,
                                @Valid @RequestBody UpdateProfileResource body) {
        commandService.handle(new UpdateProfileCommand(user.userId(), body.heightCm(), body.sex(), body.activityLevel()));
        return queryService.handle(new GetHealthSummaryQuery(user.userId()));
    }
}
