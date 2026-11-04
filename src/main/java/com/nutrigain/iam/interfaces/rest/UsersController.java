package com.nutrigain.iam.interfaces.rest;

import com.nutrigain.iam.domain.model.queries.GetUserByIdQuery;
import com.nutrigain.iam.domain.services.UserQueryService;
import com.nutrigain.iam.interfaces.rest.resources.UserResource;
import com.nutrigain.iam.interfaces.rest.transform.UserResourceAssembler;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import com.nutrigain.shared.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
public class UsersController {

    private final UserQueryService userQueryService;

    public UsersController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/me")
    public UserResource me(@AuthenticationPrincipal AuthenticatedUser principal) {
        return userQueryService.handle(new GetUserByIdQuery(principal.userId()))
                .map(UserResourceAssembler::toResource)
                .orElseThrow(() -> new ResourceNotFoundException("User", principal.userId()));
    }
}
