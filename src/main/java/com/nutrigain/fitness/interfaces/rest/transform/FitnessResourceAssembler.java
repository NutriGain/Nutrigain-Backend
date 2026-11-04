package com.nutrigain.fitness.interfaces.rest.transform;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import com.nutrigain.fitness.domain.model.aggregates.FitnessPath;
import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import com.nutrigain.fitness.interfaces.rest.resources.BodyWeightResource;
import com.nutrigain.fitness.interfaces.rest.resources.ExerciseResource;
import com.nutrigain.fitness.interfaces.rest.resources.FitnessPathResource;
import com.nutrigain.fitness.interfaces.rest.resources.RoutineDayResource;
import com.nutrigain.fitness.interfaces.rest.resources.WeeklyProgressResource;

public final class FitnessResourceAssembler {
    private FitnessResourceAssembler() {
    }

    public static FitnessPathResource toResource(FitnessPath path) {
        return new FitnessPathResource(path.getId(), path.getType().name(), path.getName(),
                path.getDescription(), path.getImageUrl());
    }

    public static RoutineDayResource toResource(RoutineDay day) {
        return new RoutineDayResource(day.getId(), day.getDayOfWeek(),
                day.getExercises().stream().map(e -> new ExerciseResource(e.name(), e.sets(), e.reps())).toList());
    }

    public static WeeklyProgressResource toResource(WeeklyProgress p) {
        return new WeeklyProgressResource(p.getWeekStart(), p.getPlannedWorkouts(), p.completedWorkouts(),
                p.completionPercentage(), p.getTargetWeightKg(), p.getCompletedRoutineDayIds());
    }

    public static BodyWeightResource toResource(BodyWeightEntry e) {
        return new BodyWeightResource(e.getId(), e.getRecordedOn(), e.getWeightKg());
    }
}
