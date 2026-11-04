package com.nutrigain.gamification.application.internal.queryservices;

import com.nutrigain.gamification.domain.model.aggregates.Avatar;
import com.nutrigain.gamification.domain.model.aggregates.MotivationalMessage;
import com.nutrigain.gamification.domain.model.aggregates.Streak;
import com.nutrigain.gamification.domain.model.queries.GetAvatarByUserIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetRandomMotivationalMessageQuery;
import com.nutrigain.gamification.domain.model.queries.GetStreakByUserIdQuery;
import com.nutrigain.gamification.domain.services.ProgressionQueryService;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.AvatarRepository;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.MotivationalMessageRepository;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.StreakRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional(readOnly = true)
public class ProgressionQueryServiceImpl implements ProgressionQueryService {

    private final AvatarRepository avatarRepository;
    private final StreakRepository streakRepository;
    private final MotivationalMessageRepository messageRepository;

    public ProgressionQueryServiceImpl(AvatarRepository avatarRepository, StreakRepository streakRepository,
                                       MotivationalMessageRepository messageRepository) {
        this.avatarRepository = avatarRepository;
        this.streakRepository = streakRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<Avatar> handle(GetAvatarByUserIdQuery query) {
        return avatarRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<Streak> handle(GetStreakByUserIdQuery query) {
        return streakRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<MotivationalMessage> handle(GetRandomMotivationalMessageQuery query) {
        List<MotivationalMessage> all = messageRepository.findAll();
        if (all.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(all.get(ThreadLocalRandom.current().nextInt(all.size())));
    }
}
