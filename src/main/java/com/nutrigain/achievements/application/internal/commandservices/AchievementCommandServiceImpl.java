package com.nutrigain.achievements.application.internal.commandservices;

import com.nutrigain.achievements.domain.model.aggregates.AchievementTracker;
import com.nutrigain.achievements.domain.model.aggregates.UserBadge;
import com.nutrigain.achievements.domain.model.commands.RecordActivityStatsCommand;
import com.nutrigain.achievements.domain.model.commands.RecordChallengeCommand;
import com.nutrigain.achievements.domain.model.commands.RecordWorkoutCommand;
import com.nutrigain.achievements.domain.model.valueobjects.BadgeType;
import com.nutrigain.achievements.domain.services.AchievementCommandService;
import com.nutrigain.achievements.infrastructure.persistence.jpa.repositories.AchievementTrackerRepository;
import com.nutrigain.achievements.infrastructure.persistence.jpa.repositories.UserBadgeRepository;
import com.nutrigain.shared.application.DomainEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AchievementCommandServiceImpl implements AchievementCommandService {

    private final AchievementTrackerRepository trackerRepository;
    private final UserBadgeRepository badgeRepository;
    private final DomainEventPublisher eventPublisher;
    private final Clock clock;

    public AchievementCommandServiceImpl(AchievementTrackerRepository trackerRepository,
                                         UserBadgeRepository badgeRepository,
                                         DomainEventPublisher eventPublisher, Clock clock) {
        this.trackerRepository = trackerRepository;
        this.badgeRepository = badgeRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void handle(RecordWorkoutCommand command) {
        AchievementTracker tracker = tracker(command.userId());
        tracker.workoutCompleted();
        evaluate(tracker);
    }

    @Override
    @Transactional
    public void handle(RecordChallengeCommand command) {
        AchievementTracker tracker = tracker(command.userId());
        tracker.challengeCompleted(command.extreme());
        evaluate(tracker);
    }

    @Override
    @Transactional
    public void handle(RecordActivityStatsCommand command) {
        AchievementTracker tracker = tracker(command.userId());
        tracker.activityStats(command.currentStreak(), command.level());
        evaluate(tracker);
    }

    private AchievementTracker tracker(Long userId) {
        return trackerRepository.findByUserId(userId).orElseGet(() -> new AchievementTracker(userId));
    }

    /** Saves the tracker and awards every badge that is now earned and not yet owned. */
    private void evaluate(AchievementTracker tracker) {
        tracker = trackerRepository.save(tracker);
        Set<BadgeType> owned = badgeRepository.findByUserIdOrderByIdAsc(tracker.getUserId()).stream()
                .map(UserBadge::getBadge).collect(Collectors.toSet());
        LocalDate today = LocalDate.now(clock);
        for (BadgeType badge : tracker.earnedBadges()) {
            if (!owned.contains(badge)) {
                UserBadge awarded = badgeRepository.save(UserBadge.award(tracker.getUserId(), badge, today));
                eventPublisher.publishFrom(awarded);
            }
        }
    }
}
