package com.nutrigain.profile.domain.services;

import com.nutrigain.profile.domain.model.aggregates.UserProfile;
import com.nutrigain.profile.domain.model.commands.UpdateProfileCommand;

public interface ProfileCommandService {
    /** Creates the profile the first time and updates it afterwards. */
    UserProfile handle(UpdateProfileCommand command);
}
