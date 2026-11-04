package com.nutrigain.fitness.infrastructure.seed;

import com.nutrigain.fitness.domain.model.aggregates.FitnessPath;
import com.nutrigain.fitness.domain.model.valueobjects.PathType;
import com.nutrigain.fitness.infrastructure.persistence.jpa.repositories.FitnessPathRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/** Seeds the three fitness paths (ids 1, 2 and 3 on an empty database). Runs before the other seeders. */
@Component
@Order(1)
public class FitnessPathSeeder implements ApplicationRunner {

    private final FitnessPathRepository repository;

    public FitnessPathSeeder(FitnessPathRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                new FitnessPath(PathType.MUSCLE_GROWTH, "Crecimiento muscular",
                        "Enfocado en ganar masa muscular", "musculacion.png"),
                new FitnessPath(PathType.AGILITY, "Deportes de agilidad",
                        "Mejora tu velocidad y coordinacion", "agilidad.png"),
                new FitnessPath(PathType.YOGA, "Yoga",
                        "Flexibilidad, balance y enfoque", "yoga.png")));
    }
}
