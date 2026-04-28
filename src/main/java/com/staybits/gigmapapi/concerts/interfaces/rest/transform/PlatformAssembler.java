package com.staybits.gigmapapi.concerts.interfaces.rest.transform;

import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.interfaces.rest.resources.PlatformResource;

public class PlatformAssembler {
    
    public static PlatformResource toResourceFromEntity(Platform entity) {
        if (entity == null) return null;
        return new PlatformResource(
            entity.getName(),
            entity.getImageUrl()
        );
    }

    public static Platform toEntityFromResource(PlatformResource resource) {
        if (resource == null) return null;
        return new Platform(
            resource.platformName(),
            resource.platformImage()
        );
    }
}
