package com.nutrigain.fitness.interfaces.acl;

import com.nutrigain.fitness.domain.model.aggregates.BodyWeightEntry;
import com.nutrigain.fitness.domain.model.queries.GetBodyWeightHistoryQuery;
import com.nutrigain.fitness.domain.model.queries.GetFitnessPathByIdQuery;
import com.nutrigain.fitness.domain.services.FitnessPathQueryService;
import com.nutrigain.fitness.domain.services.TrainingProgressQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Public entry point of the Fitness context for other bounded contexts.
 * Only primitives leave this boundary, never Fitness aggregates.
 */
@Service
public class FitnessContextFacade {

    private final FitnessPathQueryService fitnessPathQueryService;
    private final TrainingProgressQueryService progressQueryService;

    public FitnessContextFacade(FitnessPathQueryService fitnessPathQueryService,
                                TrainingProgressQueryService progressQueryService) {
        this.fitnessPathQueryService = fitnessPathQueryService;
        this.progressQueryService = progressQueryService;
    }

    public boolean existsFitnessPathById(Long fitnessPathId) {
        return fitnessPathQueryService.handle(new GetFitnessPathByIdQuery(fitnessPathId)).isPresent();
    }

    /** Stable code of the path (MUSCLE_GROWTH, AGILITY, YOGA). */
    public Optional<String> fetchFitnessPathCode(Long fitnessPathId) {
        return fitnessPathQueryService.handle(new GetFitnessPathByIdQuery(fitnessPathId))
                .map(path -> path.getType().name());
    }

    /** Most recent registered body weight of the user, if any. */
    public Optional<BigDecimal> fetchLatestWeightKg(Long userId) {
        List<BodyWeightEntry> history = progressQueryService.handle(new GetBodyWeightHistoryQuery(userId));
        return history.isEmpty() ? Optional.empty() : Optional.of(history.get(history.size() - 1).getWeightKg());
    }
}
