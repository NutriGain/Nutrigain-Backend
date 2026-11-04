package com.nutrigain.fitness.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BodyWeightResource(Long id, LocalDate recordedOn, BigDecimal weightKg) {
}
