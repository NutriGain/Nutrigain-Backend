package com.nutrigain.fitness.domain.services;

import com.nutrigain.fitness.domain.model.aggregates.FitnessPath;
import com.nutrigain.fitness.domain.model.queries.GetAllFitnessPathsQuery;
import com.nutrigain.fitness.domain.model.queries.GetFitnessPathByIdQuery;

import java.util.List;
import java.util.Optional;

public interface FitnessPathQueryService {
    List<FitnessPath> handle(GetAllFitnessPathsQuery query);

    Optional<FitnessPath> handle(GetFitnessPathByIdQuery query);
}
