package com.nutrigain.gamification.interfaces.rest.transform;

import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import com.nutrigain.gamification.domain.model.aggregates.Challenge;
import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.aggregates.MotivationalMessage;
import com.nutrigain.gamification.domain.model.aggregates.Streak;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.AvatarResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.ChallengeAssignmentResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.ChallengeResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.MotivationalMessageResource;
import com.nutrigain.gamification.interfaces.rest.resources.GamificationResources.StreakResource;

import java.time.LocalDate;

public final class GamificationResourceAssembler {
    private GamificationResourceAssembler() {
    }

    public static AvatarResource toResource(Avatar a) {
        return new AvatarResource(a.getId(), a.getCharacterName(), a.getImageUrl(), a.level(), a.getExperience(),
                a.experienceToNextLevel(), a.getMood());
    }

    public static StreakResource toResource(Streak s, LocalDate today) {
        return new StreakResource(s.currentDaysAsOf(today), s.getLongestDays(), s.getLastActivityDate());
    }

    public static ChallengeResource toResource(Challenge c) {
        return new ChallengeResource(c.getId(), c.getTitle(), c.getDescription(), c.getDifficulty(),
                c.getCategory(), c.getXpReward());
    }

    public static ChallengeAssignmentResource toResource(ChallengeAssignment a, Challenge c) {
        return new ChallengeAssignmentResource(a.getId(), a.getKind(), a.getStatus(), a.getAssignedOn(),
                a.getCompletedOn(), a.getXpReward(), toResource(c));
    }

    public static MotivationalMessageResource toResource(MotivationalMessage m) {
        return new MotivationalMessageResource(m.getId(), m.getText());
    }
}
