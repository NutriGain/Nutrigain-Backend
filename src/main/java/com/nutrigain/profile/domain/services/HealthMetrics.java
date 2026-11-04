package com.nutrigain.profile.domain.services;

import com.nutrigain.profile.domain.model.valueobjects.ActivityLevel;
import com.nutrigain.profile.domain.model.valueobjects.BmiCategory;
import com.nutrigain.profile.domain.model.valueobjects.Sex;

/** Pure domain formulas: body mass index and daily calories (Mifflin-St Jeor). */
public final class HealthMetrics {

    private HealthMetrics() {
    }

    public static double bmi(double weightKg, int heightCm) {
        double meters = heightCm / 100.0;
        return Math.round(weightKg / (meters * meters) * 10.0) / 10.0;
    }

    public static BmiCategory category(double bmi) {
        if (bmi < 18.5) {
            return BmiCategory.UNDERWEIGHT;
        }
        if (bmi < 25) {
            return BmiCategory.NORMAL;
        }
        if (bmi < 30) {
            return BmiCategory.OVERWEIGHT;
        }
        return BmiCategory.OBESE;
    }

    public static int dailyCalories(Sex sex, double weightKg, int heightCm, int age, ActivityLevel level) {
        double basal = 10 * weightKg + 6.25 * heightCm - 5 * age + (sex == Sex.MALE ? 5 : -161);
        return (int) Math.round(basal * level.factor());
    }
}
