package com.nutrigain.profile.domain.model.commands;

import com.nutrigain.profile.domain.model.valueobjects.ActivityLevel;
import com.nutrigain.profile.domain.model.valueobjects.Sex;

public record UpdateProfileCommand(Long userId, int heightCm, Sex sex, ActivityLevel activityLevel) {
}
