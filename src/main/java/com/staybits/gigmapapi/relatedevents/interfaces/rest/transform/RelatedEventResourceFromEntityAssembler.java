package com.staybits.gigmapapi.relatedevents.interfaces.rest.transform;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.VenueAssembler;
import com.staybits.gigmapapi.relatedevents.domain.model.aggregates.RelatedEvent;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.RelatedEventResource;

import java.util.List;

public class RelatedEventResourceFromEntityAssembler {

    public static RelatedEventResource toResourceFromEntity(RelatedEvent entity) {
        if (entity == null) return null;

        return new RelatedEventResource(
                entity.getId(),
                entity.getConcert().getId(),
                entity.getTitulo(),
                entity.getDatehour(),
                entity.getDescripcion(),
                entity.getTipo().name(),
                VenueAssembler.toResourceFromEntity(entity.getVenue()),
                entity.getStatus().name(),
                entity.getOrganizador().getId(),
                entity.getParticipantes().stream().map(User::getId).toList()
        );
    }

    public static List<RelatedEventResource> toResourcesFromEntities(List<RelatedEvent> entities) {
        return entities.stream()
                .map(RelatedEventResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
