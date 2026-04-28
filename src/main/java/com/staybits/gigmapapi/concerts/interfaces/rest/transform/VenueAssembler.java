package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.VenueResource;

public class VenueAssembler {
    
    public static VenueResource toResourceFromEntity(Venue entity) {
        if (entity == null) return null;
        return new VenueResource(
            entity.getName(),
            entity.getDireccion(),
            entity.getLatitud(),
            entity.getLongitud(),
            entity.getCapacidad()
        );
    }

    public static Venue toEntityFromResource(VenueResource resource) {
        if (resource == null) return null;
        return new Venue(
            resource.name(),
            resource.latitude(),
            resource.longitude(),
            resource.address(),
            resource.capacity()
        );
    }
}
