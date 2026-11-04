package com.nutrigain.iam.domain.services;

import com.nutrigain.iam.domain.model.aggregates.User;
import com.nutrigain.iam.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
}
