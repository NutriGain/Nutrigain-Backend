package com.nutrigain.fitness.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDate;

/** recordedOn may be null, meaning today. */
public record RegisterBodyWeightCommand(Long userId, BigDecimal weightKg, LocalDate recordedOn) {
}
