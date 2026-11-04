package com.nutrigain.fitness.domain.services;

import com.nutrigain.fitness.domain.model.aggregates.RoutineDay;
import com.nutrigain.fitness.domain.model.queries.GetRoutinesByUserIdQuery;

import java.util.List;

public interface RoutineQueryService {
    List<RoutineDay> handle(GetRoutinesByUserIdQuery query);
}
