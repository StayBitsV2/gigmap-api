package com.staybits.gigmapapi.connections.interfaces.rest.transform;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.connections.domain.model.aggregates.Connection;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.ConnectionResource;

import java.util.List;

public class ConnectionResourceFromEntityAssembler {
    public static ConnectionResource toResourceFromEntity(Connection entity, User currentUser) {
        if (entity == null) return null;
        User other = entity.getOtherUser(currentUser);
        return new ConnectionResource(
                entity.getId(),
                other.getId(),
                other.getUsername(),
                other.getImagenUrl(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }

    public static List<ConnectionResource> toResourcesFromEntities(List<Connection> entities, User currentUser) {
        return entities.stream()
                .map(connection -> toResourceFromEntity(connection, currentUser))
                .toList();
    }
}
