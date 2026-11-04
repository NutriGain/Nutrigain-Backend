package com.nutrigain.fitness.application.internal.commandservices;

import com.nutrigain.fitness.domain.model.aggregates.FitnessPath;
import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import com.nutrigain.fitness.domain.model.commands.GenerateWeeklyRoutineCommand;
import com.nutrigain.fitness.domain.services.RoutineCommandService;
import com.nutrigain.fitness.domain.services.RoutineTemplates;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.FitnessPathRepository;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.RoutineDayRepository;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoutineCommandServiceImpl implements RoutineCommandService {

    private final FitnessPathRepository fitnessPathRepository;
    private final RoutineDayRepository routineDayRepository;

    public RoutineCommandServiceImpl(FitnessPathRepository fitnessPathRepository,
                                     RoutineDayRepository routineDayRepository) {
        this.fitnessPathRepository = fitnessPathRepository;
        this.routineDayRepository = routineDayRepository;
    }

    /** Replaces the user's weekly routine with the default one of the chosen path. */
    @Override
    @Transactional
    public List<RoutineDay> handle(GenerateWeeklyRoutineCommand command) {
        FitnessPath path = fitnessPathRepository.findById(command.fitnessPathId())
                .orElseThrow(() -> new ResourceNotFoundException("FitnessPath", command.fitnessPathId()));
        routineDayRepository.deleteByUserId(command.userId());
        routineDayRepository.flush();
        List<RoutineDay> days = RoutineTemplates.forPath(path.getType()).entrySet().stream()
                .map(e -> new RoutineDay(command.userId(), e.getKey(), e.getValue()))
                .toList();
        return routineDayRepository.saveAll(days);
    }
}
