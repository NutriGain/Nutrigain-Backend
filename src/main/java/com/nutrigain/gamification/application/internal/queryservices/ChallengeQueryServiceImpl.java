package com.nutrigain.gamification.application.internal.queryservices;

import com.nutrigain.gamification.domain.model.aggregates.Challenge;
import com.nutrigain.gamification.domain.model.aggregates.ChallengeAssignment;
import com.nutrigain.gamification.domain.model.queries.GetAllChallengesQuery;
import com.nutrigain.gamification.domain.model.queries.GetAssignmentsByUserIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetChallengeByIdQuery;
import com.nutrigain.gamification.domain.model.queries.GetTodaysDailyExtremeQuery;
import com.nutrigain.gamification.domain.model.valueobjects.AssignmentKind;
import com.nutrigain.gamification.domain.services.ChallengeQueryService;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.ChallengeAssignmentRepository;
import com.nutrigain.gamification.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeAssignmentRepository assignmentRepository;
    private final Clock clock;

    public ChallengeQueryServiceImpl(ChallengeRepository challengeRepository,
                                     ChallengeAssignmentRepository assignmentRepository, Clock clock) {
        this.challengeRepository = challengeRepository;
        this.assignmentRepository = assignmentRepository;
        this.clock = clock;
    }

    @Override
    public List<Challenge> handle(GetAllChallengesQuery query) {
        return query.category() == null ? challengeRepository.findAll() : challengeRepository.findByCategory(query.category());
    }

    @Override
    public Optional<Challenge> handle(GetChallengeByIdQuery query) {
        return challengeRepository.findById(query.challengeId());
    }

    @Override
    public List<ChallengeAssignment> handle(GetAssignmentsByUserIdQuery query) {
        return assignmentRepository.findByUserIdOrderByIdDesc(query.userId());
    }

    @Override
    public Optional<ChallengeAssignment> handle(GetTodaysDailyExtremeQuery query) {
        return assignmentRepository.findByUserIdAndKindAndAssignedOn(
                query.userId(), AssignmentKind.DAILY_EXTREME, LocalDate.now(clock));
    }
}
