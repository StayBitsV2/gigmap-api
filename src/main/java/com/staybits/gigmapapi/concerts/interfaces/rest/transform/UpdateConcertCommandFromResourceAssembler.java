package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.commands.UpdateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.UpdateConcertResource;

public class UpdateConcertCommandFromResourceAssembler {

    public static UpdateConcertCommand toCommandFromResource(UpdateConcertResource resource) {
        if (resource == null) return null;
        
        return new UpdateConcertCommand(
            resource.id(),
            resource.title(),
            resource.date(),
            resource.description(),
            resource.imageUrl(),
            VenueAssembler.toEntityFromResource(resource.venue()),
            ConcertStatus.valueOf(resource.status())
        );
    }
}
