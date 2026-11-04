package com.nutrigain.nutrition.domain.model.commands;

public record AddMealToManualSelectionCommand(Long userId, Long mealId) {
}
