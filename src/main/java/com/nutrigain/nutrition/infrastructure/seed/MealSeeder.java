package com.nutrigain.nutrition.infrastructure.seed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutrigain.nutrition.domain.model.aggregates.Meal;
import com.nutrigain.nutrition.domain.model.valueobjects.Macros;
import com.nutrigain.nutrition.domain.model.valueobjects.MealType;
import com.nutrigain.nutrition.infrastructure.persistence.jpa.repositories.MealRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/** Seeds the meal catalog from seed/meals.json when the table is empty. */
@Component
@Order(2)
public class MealSeeder implements ApplicationRunner {

    private final MealRepository repository;
    private final ObjectMapper objectMapper;

    public MealSeeder(MealRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        if (repository.count() > 0) {
            return;
        }
        List<Meal> meals = new ArrayList<>();
        try (InputStream in = new ClassPathResource("seed/meals.json").getInputStream()) {
            for (JsonNode n : objectMapper.readTree(in)) {
                meals.add(new Meal(n.get("name").asText(), MealType.valueOf(n.get("mealType").asText()),
                        new Macros(n.get("calories").asInt(), n.get("proteins").asInt(),
                                n.get("carbohydrates").asInt(), n.get("fats").asInt()),
                        n.get("imageUrl").asText()));
            }
        }
        repository.saveAll(meals);
    }
}
