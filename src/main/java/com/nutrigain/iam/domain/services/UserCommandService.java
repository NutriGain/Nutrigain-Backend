package com.nutrigain.iam.domain.services;

import com.nutrigain.iam.domain.model.aggregates.User;
import com.nutrigain.iam.domain.model.commands.SignInCommand;
import com.nutrigain.iam.domain.model.commands.SignUpCommand;
import com.nutrigain.iam.domain.model.valueobjects.AuthenticationResult;

public interface UserCommandService {
    User handle(SignUpCommand command);

    AuthenticationResult handle(SignInCommand command);
}
