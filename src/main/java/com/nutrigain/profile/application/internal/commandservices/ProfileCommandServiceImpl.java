package com.nutrigain.profile.application.internal.commandservices;

import com.nutrigain.profile.domain.model.aggregates.UserProfile;
import com.nutrigain.profile.domain.model.commands.UpdateProfileCommand;
import com.nutrigain.profile.domain.services.ProfileCommandService;
import com.nutrigain.profile.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final UserProfileRepository repository;

    public ProfileCommandServiceImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserProfile handle(UpdateProfileCommand command) {
        UserProfile profile = repository.findByUserId(command.userId()).orElse(null);
        if (profile == null) {
            profile = new UserProfile(command.userId(), command.heightCm(), command.sex(), command.activityLevel());
        } else {
            profile.update(command.heightCm(), command.sex(), command.activityLevel());
        }
        return repository.save(profile);
    }
}
