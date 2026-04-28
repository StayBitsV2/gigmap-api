package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.UpdateCommunityCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.UpdateCommunityResource;

public class UpdateCommunityCommandFromResourceAssembler {
    public static UpdateCommunityCommand toCommandFromResource(Long id, UpdateCommunityResource resource) {
        return new UpdateCommunityCommand(id, resource.name(), resource.image(), resource.description());
    }
}
