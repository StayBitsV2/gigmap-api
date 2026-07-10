package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.CreateThreadCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreateThreadResource;

public class CreateThreadCommandFromResourceAssembler {
    public static CreateThreadCommand toCommandFromResource(CreateThreadResource resource) {
        return new CreateThreadCommand(resource.title(), resource.content(), resource.imageUrl(), resource.communityId(), resource.userId());
    }
}
