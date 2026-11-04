package com.nutrigain.iam.application.internal.outboundservices.acl;

import com.nutrigain.fitness.interfaces.acl.FitnessContextFacade;
import org.springframework.stereotype.Service;

/** Anti-corruption layer between IAM and the Fitness bounded context. */
@Service
public class ExternalFitnessService {

    private final FitnessContextFacade fitnessContextFacade;

    public ExternalFitnessService(FitnessContextFacade fitnessContextFacade) {
        this.fitnessContextFacade = fitnessContextFacade;
    }

    public boolean fitnessPathExists(Long fitnessPathId) {
        return fitnessContextFacade.existsFitnessPathById(fitnessPathId);
    }
}
