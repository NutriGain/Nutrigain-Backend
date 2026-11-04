package com.nutrigain.gamification.domain.model.valueobjects;

/** The virtual companion that goes with each fitness path. */
public record AvatarCharacter(String name, String imageUrl) {

    public static AvatarCharacter forFitnessPathCode(String code) {
        if ("MUSCLE_GROWTH".equals(code)) {
            return new AvatarCharacter("Titan", "avatar1.png");
        }
        if ("AGILITY".equals(code)) {
            return new AvatarCharacter("Rayo", "avatar2.png");
        }
        return new AvatarCharacter("Zen", "avatar3.png");
    }
}
