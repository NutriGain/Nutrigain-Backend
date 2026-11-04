package com.nutrigain.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(UserResource user, String token) {
}
