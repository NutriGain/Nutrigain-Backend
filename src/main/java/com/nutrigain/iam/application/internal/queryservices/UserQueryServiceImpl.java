package com.nutrigain.iam.application.internal.queryservices;

import com.nutrigain.iam.domain.model.aggregates.User;
import com.nutrigain.iam.domain.model.queries.GetUserByIdQuery;
import com.nutrigain.iam.domain.services.UserQueryService;
import com.nutrigain.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }
}
