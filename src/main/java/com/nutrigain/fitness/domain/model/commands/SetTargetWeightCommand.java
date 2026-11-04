package com.nutrigain.fitness.domain.model.commands;

import java.math.BigDecimal;

public record SetTargetWeightCommand(Long userId, BigDecimal targetWeightKg) {
}
