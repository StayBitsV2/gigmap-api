package com.staybits.gigmapapi.authentication.interfaces.rest.transform;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(user.getId(), user.getEmail(), user.getUsername(), user.getName(), user.getRole().name(), user.getImagenUrl());
    }
}
