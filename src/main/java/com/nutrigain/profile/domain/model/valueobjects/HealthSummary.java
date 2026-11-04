package com.nutrigain.profile.domain.model.valueobjects;

import java.math.BigDecimal;

/** Read model: profile data plus the metrics derived from it. Derived fields are null when data is missing. */
public record HealthSummary(Integer heightCm, Sex sex, ActivityLevel activityLevel, BigDecimal weightKg,
                            Double bmi, BmiCategory bmiCategory, Integer dailyCalorieTarget) {
}
