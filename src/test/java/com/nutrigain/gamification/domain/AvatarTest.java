package com.nutrigain.gamification.domain;

import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import com.nutrigain.gamification.domain.model.valueobjects.AvatarCharacter;
import com.nutrigain.gamification.domain.model.valueobjects.Mood;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AvatarTest {

    private Avatar newAvatar() {
        return new Avatar(1L, AvatarCharacter.forFitnessPathCode("YOGA"));
    }

    @Test
    void startsAtLevelOne() {
        Avatar avatar = newAvatar();
        assertEquals(1, avatar.level());
        assertEquals(100, avatar.experienceToNextLevel());
    }

    @Test
    void levelsUpEveryHundredExperiencePoints() {
        Avatar avatar = newAvatar();
        avatar.gainExperience(60);
        avatar.gainExperience(60);
        assertEquals(2, avatar.level());
        assertEquals(80, avatar.experienceToNextLevel());
    }

    @Test
    void bigRewardsMakeTheAvatarEnergetic() {
        Avatar avatar = newAvatar();
        avatar.gainExperience(100);
        assertEquals(Mood.ENERGETIC, avatar.getMood());
    }

    @Test
    void rejectsNonPositiveExperience() {
        assertThrows(IllegalArgumentException.class, () -> newAvatar().gainExperience(0));
    }
}
