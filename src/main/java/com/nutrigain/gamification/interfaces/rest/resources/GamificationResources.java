package com.nutrigain.gamification.interfaces.rest.resources;

import com.nutrigain.gamification.domain.model.valueobjects.AssignmentKind;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentStatus;
import com.nutrigain.gamification.domain.model.valueobjects.ChallengeCategory;
import com.nutrigain.gamification.domain.model.valueobjects.Difficulty;
import com.nutrigain.gamification.domain.model.valueobjects.Mood;

import java.time.LocalDate;

/** Response shapes of the Gamification context. */
public final class GamificationResources {

    private GamificationResources() {
    }

    public record AvatarResource(Long id, String characterName, String imageUrl, int level, int experience,
                                 int experienceToNextLevel, Mood mood) {
    }

    public record StreakResource(int currentDays, int longestDays, LocalDate lastActivityDate) {
    }

    public record ChallengeResource(Long id, String title, String description, Difficulty difficulty,
                                    ChallengeCategory category, int xpReward) {
    }

    public record ChallengeAssignmentResource(Long id, AssignmentKind kind, AssignmentStatus status,
                                              LocalDate assignedOn, LocalDate completedOn, int xpReward,
                                              ChallengeResource challenge) {
    }

    public record MotivationalMessageResource(Long id, String text) {
    }
}
