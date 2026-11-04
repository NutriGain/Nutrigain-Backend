package com.nutrigain.achievements.domain.model.commands;

public record RecordActivityStatsCommand(Long userId, int currentStreak, int level) {
}
