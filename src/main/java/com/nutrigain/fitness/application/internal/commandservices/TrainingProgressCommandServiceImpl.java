package com.nutrigain.fitness.application.internal.commandservices;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import com.nutrigain.fitness.domain.model.aggregates.WeeklyProgress;
import com.nutrigain.fitness.domain.model.commands.CompleteWorkoutCommand;
import com.nutrigain.fitness.domain.model.commands.RegisterBodyWeightCommand;
import com.nutrigain.fitness.domain.model.commands.SetTargetWeightCommand;
import com.nutrigain.fitness.domain.services.TrainingProgressCommandService;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.BodyWeightEntryRepository;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.RoutineDayRepository;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.WeeklyProgressRepository;
import com.nutrigain.shared.application.DomainEventPublisher;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class TrainingProgressCommandServiceImpl implements TrainingProgressCommandService {

    private final RoutineDayRepository routineDayRepository;
    private final WeeklyProgressRepository weeklyProgressRepository;
    private final BodyWeightEntryRepository bodyWeightEntryRepository;
    private final DomainEventPublisher eventPublisher;
    private final Clock clock;

    public TrainingProgressCommandServiceImpl(RoutineDayRepository routineDayRepository,
                                              WeeklyProgressRepository weeklyProgressRepository,
                                              BodyWeightEntryRepository bodyWeightEntryRepository,
                                              DomainEventPublisher eventPublisher, Clock clock) {
        this.routineDayRepository = routineDayRepository;
        this.weeklyProgressRepository = weeklyProgressRepository;
        this.bodyWeightEntryRepository = bodyWeightEntryRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    @Transactional
    public WeeklyProgress handle(CompleteWorkoutCommand command) {
        RoutineDay routineDay = routineDayRepository.findByIdAndUserId(command.routineDayId(), command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("RoutineDay", command.routineDayId()));
        LocalDate today = LocalDate.now(clock);
        WeeklyProgress progress = currentWeek(command.userId(), today);
        progress.completeWorkout(routineDay.getId(), today);
        progress = weeklyProgressRepository.save(progress);
        eventPublisher.publishFrom(progress);
        return progress;
    }

    @Override
    @Transactional
    public WeeklyProgress handle(SetTargetWeightCommand command) {
        WeeklyProgress progress = currentWeek(command.userId(), LocalDate.now(clock));
        progress.changeTargetWeight(command.targetWeightKg());
        return weeklyProgressRepository.save(progress);
    }

    @Override
    @Transactional
    public BodyWeightEntry handle(RegisterBodyWeightCommand command) {
        LocalDate date = command.recordedOn() != null ? command.recordedOn() : LocalDate.now(clock);
        if (date.isAfter(LocalDate.now(clock))) {
            throw new IllegalArgumentException("Weight cannot be recorded in the future");
        }
        BodyWeightEntry entry = bodyWeightEntryRepository.findByUserIdAndRecordedOn(command.userId(), date)
                .orElseGet(() -> new BodyWeightEntry(command.userId(), date, command.weightKg()));
        entry.changeWeight(command.weightKg());
        return bodyWeightEntryRepository.save(entry);
    }

    private WeeklyProgress currentWeek(Long userId, LocalDate today) {
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int planned = (int) routineDayRepository.countByUserId(userId);
        WeeklyProgress progress = weeklyProgressRepository.findByUserIdAndWeekStart(userId, weekStart)
                .orElseGet(() -> new WeeklyProgress(userId, weekStart, planned,
                        weeklyProgressRepository.findFirstByUserIdOrderByWeekStartDesc(userId)
                                .map(WeeklyProgress::getTargetWeightKg).orElse(null)));
        progress.synchronizePlannedWorkouts(planned);
        return progress;
    }
}
