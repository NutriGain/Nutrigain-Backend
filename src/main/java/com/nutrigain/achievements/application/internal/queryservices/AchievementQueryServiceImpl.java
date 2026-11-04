package com.nutrigain.achievements.application.internal.queryservices;

import com.nutrigain.achievements.domain.model.aggregates.UserBadge;
import com.nutrigain.achievements.domain.model.queries.GetBadgesByUserIdQuery;
import com.nutrigain.achievements.domain.services.AchievementQueryService;
import com.nutrigain.achievements.infrastructure.persistence.jpa.repositories.UserBadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AchievementQueryServiceImpl implements AchievementQueryService {

    private final UserBadgeRepository repository;

    public AchievementQueryServiceImpl(UserBadgeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserBadge> handle(GetBadgesByUserIdQuery query) {
        return repository.findByUserIdOrderByIdAsc(query.userId());
    }
}
