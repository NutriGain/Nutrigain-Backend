package com.nutrigain.fitness.interfaces.rest;

import com.nutrigain.fitness.domain.model.queries.GetAllFitnessPathsQuery;
import com.nutrigain.fitness.domain.model.queries.GetFitnessPathByIdQuery;
import com.nutrigain.fitness.domain.services.FitnessPathQueryService;
import com.nutrigain.fitness.interfaces.rest.resources.FitnessPathResource;
import com.nutrigain.fitness.interfaces.rest.transform.FitnessResourceAssembler;
import com.nutrigain.shared.domain.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fitness-paths")
@Tag(name = "Fitness paths")
public class FitnessPathsController {

    private final FitnessPathQueryService queryService;

    public FitnessPathsController(FitnessPathQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<FitnessPathResource> getAll() {
        return queryService.handle(new GetAllFitnessPathsQuery()).stream()
                .map(FitnessResourceAssembler::toResource).toList();
    }

    @GetMapping("/{id}")
    public FitnessPathResource getById(@PathVariable Long id) {
        return queryService.handle(new GetFitnessPathByIdQuery(id))
                .map(FitnessResourceAssembler::toResource)
                .orElseThrow(() -> new ResourceNotFoundException("FitnessPath", id));
    }
}
