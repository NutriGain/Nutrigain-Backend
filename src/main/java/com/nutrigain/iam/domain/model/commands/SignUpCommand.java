package com.nutrigain.iam.domain.model.commands;

public record SignUpCommand(String name, String email, String password, int age, Long fitnessPathId) {
}
