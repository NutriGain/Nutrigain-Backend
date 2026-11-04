package com.nutrigain.nutrition.domain.model.commands;

public record RemoveMealFromManualSelectionCommand(Long userId, Long mealId) {
}
