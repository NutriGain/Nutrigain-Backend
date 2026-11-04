package com.nutrigain.social.domain.model.commands;

public record UpdateCompetitorScoreCommand(Long userId, int experience, int level, int currentStreak) {
}
