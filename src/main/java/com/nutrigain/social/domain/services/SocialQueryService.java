package com.nutrigain.social.domain.services;

import com.nutrigain.social.domain.model.aggregates.Competitor;
import com.nutrigain.social.domain.model.queries.GetFollowingIdsQuery;
import com.nutrigain.social.domain.model.queries.GetFriendsLeaderboardQuery;
import com.nutrigain.social.domain.model.queries.GetGlobalLeaderboardQuery;
import com.nutrigain.social.domain.model.queries.SearchCompetitorsQuery;

import java.util.List;
import java.util.Set;

public interface SocialQueryService {
    List<Competitor> handle(GetGlobalLeaderboardQuery query);

    List<Competitor> handle(GetFriendsLeaderboardQuery query);

    List<Competitor> handle(SearchCompetitorsQuery query);

    Set<Long> handle(GetFollowingIdsQuery query);
}
