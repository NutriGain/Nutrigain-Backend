package com.nutrigain.fitness.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/** recordedOn is optional and defaults to today. */
public record RegisterBodyWeightResource(
        @NotNull @DecimalMin("20.0") @DecimalMax("500.0") BigDecimal weightKg,
        LocalDate recordedOn) {
}
