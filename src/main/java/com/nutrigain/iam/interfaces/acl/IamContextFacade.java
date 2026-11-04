package com.nutrigain.iam.interfaces.acl;

import com.nutrigain.iam.domain.model.queries.GetUserByIdQuery;
import com.nutrigain.iam.domain.services.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Public entry point of IAM for other bounded contexts. Only primitives leave this boundary. */
@Service
public class IamContextFacade {

    private final UserQueryService userQueryService;

    public IamContextFacade(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    public boolean existsUserById(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId)).isPresent();
    }

    public Optional<Integer> fetchAge(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId)).map(u -> u.getAge());
    }
}
