package com.nutrigain.iam.application.internal.commandservices;

import com.nutrigain.iam.application.internal.outboundservices.acl.ExternalFitnessService;
import com.nutrigain.iam.application.internal.outboundservices.hashing.PasswordHasher;
import com.nutrigain.iam.application.internal.outboundservices.tokens.TokenService;
import com.nutrigain.iam.domain.model.aggregates.User;
import com.nutrigain.iam.domain.model.commands.SignInCommand;
import com.nutrigain.iam.domain.model.commands.SignUpCommand;
import com.nutrigain.iam.domain.model.valueobjects.AuthenticationResult;
import com.nutrigain.iam.domain.model.valueobjects.EmailAddress;
import com.nutrigain.iam.domain.services.UserCommandService;
import com.nutrigain.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.nutrigain.shared.application.DomainEventPublisher;
import com.nutrigain.shared.domain.exceptions.DuplicateResourceException;
import com.nutrigain.shared.domain.exceptions.InvalidCredentialsException;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final ExternalFitnessService externalFitnessService;
    private final DomainEventPublisher eventPublisher;

    public UserCommandServiceImpl(UserRepository userRepository, PasswordHasher passwordHasher,
                                  TokenService tokenService, ExternalFitnessService externalFitnessService,
                                  DomainEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenService = tokenService;
        this.externalFitnessService = externalFitnessService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public User handle(SignUpCommand command) {
        if (command.password() == null || command.password().length() < 6) {
            throw new IllegalArgumentException("Password must have at least 6 characters");
        }
        EmailAddress email = new EmailAddress(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already registered: " + email.value());
        }
        if (!externalFitnessService.fitnessPathExists(command.fitnessPathId())) {
            throw new ResourceNotFoundException("FitnessPath", command.fitnessPathId());
        }
        User user = new User(command.name(), email, passwordHasher.hash(command.password()),
                command.age(), command.fitnessPathId());
        user = userRepository.save(user);
        user.confirmRegistration();
        eventPublisher.publishFrom(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResult handle(SignInCommand command) {
        User user = userRepository.findByEmail(new EmailAddress(command.email()))
                .filter(u -> passwordHasher.matches(command.password(), u.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);
        return new AuthenticationResult(user, tokenService.generateToken(user.getId(), user.getEmail().value()));
    }
}
