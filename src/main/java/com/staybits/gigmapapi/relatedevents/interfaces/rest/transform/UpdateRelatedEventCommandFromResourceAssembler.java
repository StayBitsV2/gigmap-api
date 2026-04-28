package com.staybits.gigmapapi.relatedevents.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.interfaces.rest.transform.VenueAssembler;
import com.staybits.gigmapapi.relatedevents.domain.model.commands.UpdateRelatedEventCommand;
import com.staybits.gigmapapi.relatedevents.interfaces.rest.resources.UpdateRelatedEventResource;

public class UpdateRelatedEventCommandFromResourceAssembler {

    public static UpdateRelatedEventCommand toCommandFromResource(UpdateRelatedEventResource resource) {
        if (resource == null) return null;

        return new UpdateRelatedEventCommand(
                resource.id(),
                resource.titulo(),
                resource.datehour(),
                resource.descripcion(),
                VenueAssembler.toEntityFromResource(resource.venue()),
                ConcertStatus.valueOf(resource.status())
        );
    }
}
