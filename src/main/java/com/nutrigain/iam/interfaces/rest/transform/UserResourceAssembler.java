package com.nutrigain.iam.interfaces.rest.transform;

import com.nutrigain.iam.domain.model.aggregates.User;
import com.nutrigain.iam.interfaces.rest.resources.UserResource;

public final class UserResourceAssembler {
    private UserResourceAssembler() {
    }

    public static UserResource toResource(User user) {
        return new UserResource(user.getId(), user.getName(), user.getEmail().value(), user.getAge(),
                user.getFitnessPathId());
    }
}
