package com.nutrigain.achievements.domain.model.valueobjects;

public enum BadgeType {
    FIRST_WORKOUT("Primer entrenamiento", "Completa tu primera rutina"),
    TEN_WORKOUTS("Constancia", "Completa 10 entrenamientos"),
    FIRST_CHALLENGE("Retador", "Completa tu primer reto"),
    EXTREME_CHAMPION("Campeon extremo", "Completa un reto diario extremo"),
    WEEK_STREAK("Semana perfecta", "Alcanza una racha de 7 dias"),
    MONTH_STREAK("Imparable", "Alcanza una racha de 30 dias"),
    LEVEL_5("Veterano", "Llega al nivel 5");

    private final String title;
    private final String description;

    BadgeType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }
}
