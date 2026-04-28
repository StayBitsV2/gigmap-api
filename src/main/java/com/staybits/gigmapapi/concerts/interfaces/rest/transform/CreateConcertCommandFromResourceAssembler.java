package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.commands.CreateConcertCommand;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.CreateConcertResource;

public class CreateConcertCommandFromResourceAssembler {

    public static CreateConcertCommand toCommandFromResource(CreateConcertResource resource) {
        if (resource == null) return null;
        
        return new CreateConcertCommand(
            resource.title(),
            resource.date(),
            resource.description(),
            resource.imageUrl(),
            VenueAssembler.toEntityFromResource(resource.venue()),
            ConcertStatus.valueOf(resource.status()),
            resource.userId(),
            Genre.valueOf(resource.genre()),
            PlatformAssembler.toEntityFromResource(resource.platform())
        );
    }
}
