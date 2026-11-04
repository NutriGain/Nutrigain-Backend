package com.nutrigain.gamification.domain.model.commands;

import java.time.LocalDate;

/** Something worth rewarding happened: the user earns experience and keeps the streak alive. */
public record RecordActivityCommand(Long userId, int experience, LocalDate date) {
}
