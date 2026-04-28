package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateCommunityResource;

public class CreateCommunityCommandFromResourceAssembler {
    public static CreateCommunityCommand toCommandFromResource(CreateCommunityResource resource) {
        return new CreateCommunityCommand(resource.name(), resource.image(), resource.description());
    }
}
