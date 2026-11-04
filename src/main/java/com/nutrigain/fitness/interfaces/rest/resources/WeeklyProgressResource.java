package com.nutrigain.fitness.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record WeeklyProgressResource(LocalDate weekStart, int plannedWorkouts, int completedWorkouts,
                                     int completionPercentage, BigDecimal targetWeightKg,
                                     Set<Long> completedRoutineDayIds) {
}
