package com.nutrigain.nutrition.domain.model.valueobjects;

/** Nutrition's own language for what a user trains for. It is mapped from the Fitness path code by an ACL. */
public enum NutritionFocus {
    HIGH_PROTEIN,
    HIGH_CARB,
    LIGHT;

    public static NutritionFocus fromFitnessPathCode(String code) {
        if (code == null) {
            return LIGHT;
        }
        return switch (code) {
            case "MUSCLE_GROWTH" -> HIGH_PROTEIN;
            case "AGILITY" -> HIGH_CARB;
            default -> LIGHT;
        };
    }
}
