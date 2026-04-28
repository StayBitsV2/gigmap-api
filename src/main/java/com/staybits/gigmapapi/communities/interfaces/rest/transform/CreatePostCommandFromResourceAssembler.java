package com.staybits.gigmapapi.communities.interfaces.rest.transform;

import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.communities.interfaces.rest.resources.CreatePostResource;

public class CreatePostCommandFromResourceAssembler {
    public static CreatePostCommand toCommandFromResource(CreatePostResource resource) {
        return new CreatePostCommand(resource.content(), resource.imageUrl(), resource.communityId(), resource.userId());
    }
}
