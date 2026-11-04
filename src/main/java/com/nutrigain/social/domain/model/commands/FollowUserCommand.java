package com.nutrigain.social.domain.model.commands;

public record FollowUserCommand(Long followerId, Long followeeId) {
}
