package com.nutrigain.social.application.internal.queryservices;

import com.nutrigain.social.domain.model.aggregates.Competitor;
import com.nutrigain.social.domain.model.aggregates.Follow;
import com.nutrigain.social.domain.model.queries.GetFollowingIdsQuery;
import com.nutrigain.social.domain.model.queries.GetFriendsLeaderboardQuery;
import com.nutrigain.social.domain.model.queries.GetGlobalLeaderboardQuery;
import com.nutrigain.social.domain.model.queries.SearchCompetitorsQuery;
import com.nutrigain.social.domain.services.SocialQueryService;
import com.nutrigain.social.infrastructure.persistence.jpa.repositories.CompetitorRepository;
import com.nutrigain.social.infrastructure.persistence.jpa.repositories.FollowRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SocialQueryServiceImpl implements SocialQueryService {

    private static final int MAX_LIMIT = 100;

    private final CompetitorRepository competitorRepository;
    private final FollowRepository followRepository;

    public SocialQueryServiceImpl(CompetitorRepository competitorRepository, FollowRepository followRepository) {
        this.competitorRepository = competitorRepository;
        this.followRepository = followRepository;
    }

    @Override
    public List<Competitor> handle(GetGlobalLeaderboardQuery query) {
        int limit = Math.max(1, Math.min(query.limit(), MAX_LIMIT));
        return competitorRepository.findAllByOrderByExperienceDescIdAsc(PageRequest.of(0, limit));
    }

    @Override
    public List<Competitor> handle(GetFriendsLeaderboardQuery query) {
        Set<Long> ids = new HashSet<>(handle(new GetFollowingIdsQuery(query.userId())));
        ids.add(query.userId());
        return competitorRepository.findByUserIdInOrderByExperienceDescIdAsc(ids);
    }

    @Override
    public List<Competitor> handle(SearchCompetitorsQuery query) {
        if (query.name() == null || query.name().trim().length() < 2) {
            return List.of();
        }
        return competitorRepository.findTop20ByDisplayNameContainingIgnoreCaseOrderByDisplayNameAsc(query.name().trim());
    }

    @Override
    public Set<Long> handle(GetFollowingIdsQuery query) {
        return followRepository.findByFollowerId(query.userId()).stream()
                .map(Follow::getFolloweeId).collect(Collectors.toSet());
    }
}
