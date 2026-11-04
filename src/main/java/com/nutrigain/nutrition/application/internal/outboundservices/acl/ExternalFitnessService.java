package com.nutrigain.nutrition.application.internal.outboundservices.acl;

import com.nutrigain.fitness.interfaces.acl.FitnessContextFacade;
import com.nutrigain.nutrition.domain.model.valueobjects.NutritionFocus;
import org.springframework.stereotype.Service;

/** Anti-corruption layer: translates a Fitness path into the focus Nutrition understands. */
@Service("nutritionExternalFitnessService")
public class ExternalFitnessService {

    private final FitnessContextFacade fitnessContextFacade;

    public ExternalFitnessService(FitnessContextFacade fitnessContextFacade) {
        this.fitnessContextFacade = fitnessContextFacade;
    }

    public NutritionFocus fetchFocusForPath(Long fitnessPathId) {
        return NutritionFocus.fromFitnessPathCode(fitnessContextFacade.fetchFitnessPathCode(fitnessPathId).orElse(null));
    }
}
