package com.staybits.gigmapapi.relatedevents.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.VenueAssembler;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.CreateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.domain.model.valueobjects.EventType;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.CreateRelatedEventResource;

public class CreateRelatedEventCommandFromResourceAssembler {

    public static CreateRelatedEventCommand toCommandFromResource(CreateRelatedEventResource resource) {
        if (resource == null) return null;

        return new CreateRelatedEventCommand(
                resource.concertId(),
                resource.titulo(),
                resource.datehour(),
                resource.descripcion(),
                EventType.valueOf(resource.tipo()),
                VenueAssembler.toEntityFromResource(resource.venue()),
                ConcertStatus.valueOf(resource.status()),
                resource.organizadorId()
        );
    }
}
