package com.nutrigain.gamification.application.internal.outboundservices.acl;

import com.nutrigain.fitness.interfaces.acl.FitnessContextFacade;
import com.nutrigain.gamification.domain.model.valueobjects.AvatarCharacter;
import org.springframework.stereotype.Service;

/** Anti-corruption layer: translates a Fitness path into the avatar character Gamification understands. */
@Service("gamificationExternalFitnessService")
public class ExternalFitnessService {

    private final FitnessContextFacade fitnessContextFacade;

    public ExternalFitnessService(FitnessContextFacade fitnessContextFacade) {
        this.fitnessContextFacade = fitnessContextFacade;
    }

    public AvatarCharacter fetchCharacterForPath(Long fitnessPathId) {
        return AvatarCharacter.forFitnessPathCode(fitnessContextFacade.fetchFitnessPathCode(fitnessPathId).orElse(null));
    }
}
