package com.nutrigain.fitness.application.internal.queryservices;

import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import com.nutrigain.fitness.domain.model.queries.GetRoutinesByUserIdQuery;
import com.nutrigain.fitness.domain.services.RoutineQueryService;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.RoutineDayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoutineQueryServiceImpl implements RoutineQueryService {

    private final RoutineDayRepository routineDayRepository;

    public RoutineQueryServiceImpl(RoutineDayRepository routineDayRepository) {
        this.routineDayRepository = routineDayRepository;
    }

    @Override
    public List<RoutineDay> handle(GetRoutinesByUserIdQuery query) {
        return routineDayRepository.findByUserId(query.userId()).stream()
                .sorted(Comparator.comparing(RoutineDay::getDayOfWeek))
                .toList();
    }
}
