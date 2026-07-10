package com.staybits.gigmapapi.connections.interfaces.rest.transform;

import com.staybits.gigmapapi.connections.domain.model.aggregates.ConnectionRequest;
import com.staybits.gigmapapi.connections.interfaces.rest.resources.ConnectionRequestResource;

import java.util.List;

public class ConnectionRequestResourceFromEntityAssembler {
    public static ConnectionRequestResource toResourceFromEntity(ConnectionRequest entity) {
        if (entity == null) return null;
        return new ConnectionRequestResource(
                entity.getId(),
                entity.getRequester().getId(),
                entity.getTarget().getId(),
                entity.getStatus().name(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }

    public static List<ConnectionRequestResource> toResourcesFromEntities(List<ConnectionRequest> entities) {
        return entities.stream()
                .map(ConnectionRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
