package com.nutrigain.fitness.interfaces.rest.resources;

import java.time.DayOfWeek;
import java.util.List;

public record RoutineDayResource(Long id, DayOfWeek dayOfWeek, List<ExerciseResource> exercises) {
}
