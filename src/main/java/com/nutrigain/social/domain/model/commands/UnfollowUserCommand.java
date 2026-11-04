package com.nutrigain.social.domain.model.commands;

public record UnfollowUserCommand(Long followerId, Long followeeId) {
}
