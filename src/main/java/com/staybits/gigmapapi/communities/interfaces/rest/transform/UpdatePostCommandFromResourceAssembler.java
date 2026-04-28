package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.UpdatePostCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.UpdatePostResource;

public class UpdatePostCommandFromResourceAssembler {
    public static UpdatePostCommand toCommandFromResource(Long id, UpdatePostResource resource) {
        return new UpdatePostCommand(id, resource.content(), resource.image());
    }
}
