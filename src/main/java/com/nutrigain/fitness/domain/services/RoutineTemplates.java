package com.nutrigain.fitness.domain.services;

import com.nutrigain.fitness.domain.model.valueobjects.Exercise;
import com.nutrigain.fitness.domain.model.valueobjects.PathType;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Default weekly routine for each fitness path. Stateless domain knowledge. */
public final class RoutineTemplates {

    private RoutineTemplates() {
    }

    public static Map<DayOfWeek, List<Exercise>> forPath(PathType type) {
        Map<DayOfWeek, List<Exercise>> week = new LinkedHashMap<>();
        switch (type) {
            case MUSCLE_GROWTH -> {
                week.put(DayOfWeek.MONDAY, List.of(new Exercise("Sentadillas", 4, "10"),
                        new Exercise("Press banca", 4, "8"), new Exercise("Remo con barra", 3, "10")));
                week.put(DayOfWeek.WEDNESDAY, List.of(new Exercise("Peso muerto", 4, "6"),
                        new Exercise("Dominadas", 3, "8"), new Exercise("Press militar", 3, "10")));
                week.put(DayOfWeek.FRIDAY, List.of(new Exercise("Zancadas", 3, "12"),
                        new Exercise("Fondos", 3, "10"), new Exercise("Curl de biceps", 3, "12"),
                        new Exercise("Plancha", 3, "60 s")));
            }
            case AGILITY -> {
                week.put(DayOfWeek.MONDAY, List.of(new Exercise("Escalera de agilidad", 5, "30 s"),
                        new Exercise("Saltos al cajon", 4, "8"), new Exercise("Sprints", 8, "30 m")));
                week.put(DayOfWeek.TUESDAY, List.of(new Exercise("Burpees", 4, "12"),
                        new Exercise("Saltos laterales", 4, "15"), new Exercise("Skipping", 4, "30 s")));
                week.put(DayOfWeek.THURSDAY, List.of(new Exercise("Conos en zigzag", 6, "20 m"),
                        new Exercise("Salto de cuerda", 5, "1 min"), new Exercise("Zancadas con salto", 3, "10")));
                week.put(DayOfWeek.SATURDAY, List.of(new Exercise("Circuito HIIT", 5, "40 s"),
                        new Exercise("Sprints", 10, "30 m")));
            }
            case YOGA -> {
                week.put(DayOfWeek.MONDAY, List.of(new Exercise("Saludo al sol", 5, "1 ronda"),
                        new Exercise("Guerrero II", 3, "45 s"), new Exercise("Perro boca abajo", 3, "60 s")));
                week.put(DayOfWeek.WEDNESDAY, List.of(new Exercise("Postura del arbol", 3, "45 s"),
                        new Exercise("Postura del triangulo", 3, "45 s"), new Exercise("Postura de la paloma", 2, "90 s")));
                week.put(DayOfWeek.FRIDAY, List.of(new Exercise("Flujo vinyasa", 4, "5 min"),
                        new Exercise("Postura del puente", 3, "45 s"), new Exercise("Savasana", 1, "5 min")));
                week.put(DayOfWeek.SUNDAY, List.of(new Exercise("Respiracion alterna", 3, "3 min"),
                        new Exercise("Postura del nino", 2, "2 min"), new Exercise("Meditacion guiada", 1, "10 min")));
            }
        }
        return week;
    }
}
