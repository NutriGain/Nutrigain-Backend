package com.nutrigain.fitness.application.internal.queryservices;

import com.nutrigain.fitness.domain.model.aggregates.FitnessPath;
import com.nutrigain.fitness.domain.model.queries.GetAllFitnessPathsQuery;
import com.nutrigain.fitness.domain.model.queries.GetFitnessPathByIdQuery;
import com.nutrigain.fitness.domain.services.FitnessPathQueryService;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.FitnessPathRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FitnessPathQueryServiceImpl implements FitnessPathQueryService {

    private final FitnessPathRepository fitnessPathRepository;

    public FitnessPathQueryServiceImpl(FitnessPathRepository fitnessPathRepository) {
        this.fitnessPathRepository = fitnessPathRepository;
    }

    @Override
    public List<FitnessPath> handle(GetAllFitnessPathsQuery query) {
        return fitnessPathRepository.findAll();
    }

    @Override
    public Optional<FitnessPath> handle(GetFitnessPathByIdQuery query) {
        return fitnessPathRepository.findById(query.fitnessPathId());
    }
}
