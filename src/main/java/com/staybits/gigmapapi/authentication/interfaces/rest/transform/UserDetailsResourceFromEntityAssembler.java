package com.staybits.gigmapapi.authentication.interfaces.rest.transform;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.interfaces.rest.resources.UserDetailsResource;

public class UserDetailsResourceFromEntityAssembler {
    
    public static UserDetailsResource toResourceFromEntity(User entity) {
        return new UserDetailsResource(
            entity.getId(),
            entity.getEmail(),
            entity.getUsername(),
            entity.getRole().name(),
            entity.getImagenUrl(),
            entity.getDescripcion()
        );
    }
}
