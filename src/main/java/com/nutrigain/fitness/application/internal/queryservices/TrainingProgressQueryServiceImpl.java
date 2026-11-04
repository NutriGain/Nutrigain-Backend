package com.nutrigain.fitness.application.internal.queryservices;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import com.nutrigain.fitness.domain.model.queries.GetBodyWeightHistoryQuery;
import com.nutrigain.fitness.domain.model.queries.GetCurrentWeekProgressQuery;
import com.nutrigain.fitness.domain.model.queries.GetWeeklyProgressHistoryQuery;
import com.nutrigain.fitness.domain.services.TrainingProgressQueryService;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.BodyWeightEntryRepository;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.RoutineDayRepository;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.WeeklyProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrainingProgressQueryServiceImpl implements TrainingProgressQueryService {

    private final WeeklyProgressRepository weeklyProgressRepository;
    private final BodyWeightEntryRepository bodyWeightEntryRepository;
    private final RoutineDayRepository routineDayRepository;
    private final Clock clock;

    public TrainingProgressQueryServiceImpl(WeeklyProgressRepository weeklyProgressRepository,
                                            BodyWeightEntryRepository bodyWeightEntryRepository,
                                            RoutineDayRepository routineDayRepository, Clock clock) {
        this.weeklyProgressRepository = weeklyProgressRepository;
        this.bodyWeightEntryRepository = bodyWeightEntryRepository;
        this.routineDayRepository = routineDayRepository;
        this.clock = clock;
    }

    @Override
    public List<WeeklyProgress> handle(GetWeeklyProgressHistoryQuery query) {
        return weeklyProgressRepository.findByUserIdOrderByWeekStartAsc(query.userId());
    }

    /** Returns the persisted progress of this week, or a transient empty one if nothing was done yet. */
    @Override
    public WeeklyProgress handle(GetCurrentWeekProgressQuery query) {
        LocalDate weekStart = LocalDate.now(clock).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return weeklyProgressRepository.findByUserIdAndWeekStart(query.userId(), weekStart)
                .orElseGet(() -> new WeeklyProgress(query.userId(), weekStart,
                        (int) routineDayRepository.countByUserId(query.userId()),
                        weeklyProgressRepository.findFirstByUserIdOrderByWeekStartDesc(query.userId())
                                .map(WeeklyProgress::getTargetWeightKg).orElse(null)));
    }

    @Override
    public List<BodyWeightEntry> handle(GetBodyWeightHistoryQuery query) {
        return bodyWeightEntryRepository.findByUserIdOrderByRecordedOnAsc(query.userId());
    }
}
