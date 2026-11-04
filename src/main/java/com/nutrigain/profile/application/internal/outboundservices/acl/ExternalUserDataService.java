package com.nutrigain.profile.application.internal.outboundservices.acl;

import com.nutrigain.fitness.interfaces.acl.FitnessContextFacade;
import com.nutrigain.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/** Anti-corruption layer: the data Profile needs from IAM (age) and Fitness (weight). */
@Service
public class ExternalUserDataService {

    private final IamContextFacade iamContextFacade;
    private final FitnessContextFacade fitnessContextFacade;

    public ExternalUserDataService(IamContextFacade iamContextFacade, FitnessContextFacade fitnessContextFacade) {
        this.iamContextFacade = iamContextFacade;
        this.fitnessContextFacade = fitnessContextFacade;
    }

    public Optional<Integer> fetchAge(Long userId) {
        return iamContextFacade.fetchAge(userId);
    }

    public Optional<BigDecimal> fetchLatestWeightKg(Long userId) {
        return fitnessContextFacade.fetchLatestWeightKg(userId);
    }
}
