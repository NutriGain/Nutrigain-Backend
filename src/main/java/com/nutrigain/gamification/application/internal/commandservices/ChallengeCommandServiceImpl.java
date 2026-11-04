package com.nutrigain.gamification.application.internal.commandservices;

import com.nutrigain.gamification.domain.model.aggregates.Challenge;
import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.commands.AssignDailyExtremeChallengeCommand;
import com.nutrigain.gamification.domain.model.commands.CompleteChallengeCommand;
import com.nutrigain.gamification.domain.model.commands.DrawRandomChallengeCommand;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentKind;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentStatus;
import com.nutrigain.gamification.domain.model.valueobjects.Difficulty;
import com.nutrigain.gamification.domain.services.ChallengeCommandService;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.ChallengeAssignmentRepository;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.nutrigain.shared.application.DomainEventPublisher;
import com.nutrigain.shared.domain.exceptions.BusinessRuleViolationException;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    /** Each completed extreme challenge raises the next reward by this much, up to MAX_ESCALATION. */
    private static final int ESCALATION_STEP = 5;
    private static final int MAX_ESCALATION = 50;

    private final ChallengeRepository challengeRepository;
    private final ChallengeAssignmentRepository assignmentRepository;
    private final DomainEventPublisher eventPublisher;
    private final Clock clock;

    public ChallengeCommandServiceImpl(ChallengeRepository challengeRepository,
                                       ChallengeAssignmentRepository assignmentRepository,
                                       DomainEventPublisher eventPublisher, Clock clock) {
        this.challengeRepository = challengeRepository;
        this.assignmentRepository = assignmentRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    @Transactional
    public ChallengeAssignment handle(DrawRandomChallengeCommand command) {
        List<Challenge> candidates = command.category() == null
                ? challengeRepository.findByDifficultyNot(Difficulty.EXTREME)
                : challengeRepository.findByDifficultyNotAndCategory(Difficulty.EXTREME, command.category());
        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException("Challenge in category", command.category());
        }
        Challenge picked = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        return assignmentRepository.save(
                ChallengeAssignment.random(command.userId(), picked.getId(), LocalDate.now(clock), picked.getXpReward()));
    }

    /**
     * The extreme challenge of the day. It escalates: the more extreme challenges the user has completed,
     * the further along the (ordered) extreme catalog they go and the bigger the reward.
     */
    @Override
    @Transactional
    public ChallengeAssignment handle(AssignDailyExtremeChallengeCommand command) {
        LocalDate today = LocalDate.now(clock);
        var existing = assignmentRepository.findByUserIdAndKindAndAssignedOn(command.userId(), AssignmentKind.DAILY_EXTREME, today);
        if (existing.isPresent()) {
            return existing.get();
        }
        List<Challenge> extremes = challengeRepository.findByDifficultyOrderByIdAsc(Difficulty.EXTREME);
        if (extremes.isEmpty()) {
            throw new ResourceNotFoundException("Extreme challenge", "catalog is empty");
        }
        long completed = assignmentRepository.countByUserIdAndKindAndStatus(
                command.userId(), AssignmentKind.DAILY_EXTREME, AssignmentStatus.COMPLETED);
        Challenge picked = extremes.get((int) (completed % extremes.size()));
        int reward = picked.getXpReward() + (int) Math.min(completed * ESCALATION_STEP, MAX_ESCALATION);
        return assignmentRepository.save(ChallengeAssignment.dailyExtreme(command.userId(), picked.getId(), today, reward));
    }

    @Override
    @Transactional
    public ChallengeAssignment handle(CompleteChallengeCommand command) {
        ChallengeAssignment assignment = assignmentRepository.findByIdAndUserId(command.assignmentId(), command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("ChallengeAssignment", command.assignmentId()));
        if (assignment.getKind() == AssignmentKind.DAILY_EXTREME
                && !assignment.getAssignedOn().isEqual(LocalDate.now(clock))) {
            throw new BusinessRuleViolationException("The extreme challenge of a previous day has expired");
        }
        assignment.complete(LocalDate.now(clock));
        assignment = assignmentRepository.save(assignment);
        eventPublisher.publishFrom(assignment);
        return assignment;
    }
}
