package com.nutrigain.iam.interfaces.rest;

import com.nutrigain.iam.domain.model.commands.SignInCommand;
import com.nutrigain.iam.domain.model.commands.SignUpCommand;
import com.nutrigain.iam.domain.services.UserCommandService;
import com.nutrigain.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.nutrigain.iam.interfaces.rest.resources.SignInResource;
import com.nutrigain.iam.interfaces.rest.resources.SignUpResource;
import com.nutrigain.iam.interfaces.rest.resources.UserResource;
import com.nutrigain.iam.interfaces.rest.transform.UserResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserCommandService userCommandService;

    public AuthController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource signUp(@Valid @RequestBody SignUpResource resource) {
        var user = userCommandService.handle(new SignUpCommand(resource.name(), resource.email(),
                resource.password(), resource.age(), resource.fitnessPathId()));
        return UserResourceAssembler.toResource(user);
    }

    @PostMapping("/sign-in")
    public AuthenticatedUserResource signIn(@Valid @RequestBody SignInResource resource) {
        var result = userCommandService.handle(new SignInCommand(resource.email(), resource.password()));
        return new AuthenticatedUserResource(UserResourceAssembler.toResource(result.user()), result.token());
    }
}
