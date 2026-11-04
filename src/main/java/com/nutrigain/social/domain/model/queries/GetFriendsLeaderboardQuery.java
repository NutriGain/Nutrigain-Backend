package com.nutrigain.social.domain.model.queries;

/** Ranking of the users that userId follows, including userId. */
public record GetFriendsLeaderboardQuery(Long userId) {
}
